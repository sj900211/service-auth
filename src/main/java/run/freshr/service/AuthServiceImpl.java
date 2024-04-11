package run.freshr.service;

import static run.freshr.common.utils.CryptoUtil.decryptRsa;
import static run.freshr.common.utils.CryptoUtil.encryptRsa;
import static run.freshr.common.utils.MapperUtil.map;

import jakarta.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import run.freshr.common.data.EntityData;
import run.freshr.common.data.ExceptionData;
import run.freshr.common.dto.response.KeyResponse;
import run.freshr.common.security.TokenProvider;
import run.freshr.common.utils.CryptoUtil;
import run.freshr.common.utils.RestUtil;
import run.freshr.domain.account.entity.Account;
import run.freshr.domain.auth.dto.request.EncryptRequest;
import run.freshr.domain.auth.dto.request.RefreshTokenRequest;
import run.freshr.domain.auth.dto.request.SignChangePasswordRequest;
import run.freshr.domain.auth.dto.request.SignInRequest;
import run.freshr.domain.auth.dto.request.SignUpdateRequest;
import run.freshr.domain.auth.dto.response.AccountResponse;
import run.freshr.domain.auth.dto.response.EncryptResponse;
import run.freshr.domain.auth.dto.response.RefreshTokenResponse;
import run.freshr.domain.auth.dto.response.SignInResponse;
import run.freshr.domain.auth.enumerations.Role;
import run.freshr.domain.auth.redis.AccessRedis;
import run.freshr.domain.auth.redis.RefreshRedis;
import run.freshr.domain.auth.redis.RsaPair;
import run.freshr.domain.auth.unit.jpa.AccountAuthUnit;
import run.freshr.domain.auth.unit.redis.AccessRedisUnit;
import run.freshr.domain.auth.unit.redis.RefreshRedisUnit;
import run.freshr.domain.auth.unit.redis.RsaPairUnit;

/**
 * 권한 관리 service 구현 class
 *
 * @author FreshR
 * @apiNote 권한 관리 service 구현 class
 * @since 2024. 4. 2. 오후 1:06:17
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

  private final AccountAuthUnit accountAuthUnit;

  private final AccessRedisUnit accessRedisUnit;
  private final RefreshRedisUnit refreshRedisUnit;
  private final RsaPairUnit rsaPairUnit;

  private final TokenProvider provider;
  private final PasswordEncoder passwordEncoder;

  private final EntityData entityData;

  /**
   * RSA 공개키 조회
   *
   * @return public key
   * @apiNote RSA 공개키 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @Override
  @Transactional
  public ResponseEntity<?> getPublicKey() {
    KeyPair keyPar = CryptoUtil.getKeyPar();
    PublicKey publicKey = keyPar.getPublic();
    PrivateKey privateKey = keyPar.getPrivate();
    String encodePublicKey = CryptoUtil.encodePublicKey(publicKey);
    String encodePrivateKey = CryptoUtil.encodePrivateKey(privateKey);
    RsaPair redis = RsaPair.builder()
        .publicKey(encodePublicKey)
        .privateKey(encodePrivateKey)
        .createAt(LocalDateTime.now())
        .build();

    // 생성한 RSA 정보를 Redis 에 저장
    rsaPairUnit.save(redis);

    return RestUtil.ok(KeyResponse.<String>builder().key(encodePublicKey).build());
  }

  /**
   * RSA 암호화 조회
   *
   * @param dto {@link EncryptRequest}
   * @return encrypt rsa
   * @apiNote RSA 암호화 조회<br> 사용하지 않는 것을 권장<br> RSA 암호화를 할 수 없는 플랫폼일 때 사용
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @Override
  public ResponseEntity<?> getEncryptRsa(EncryptRequest dto) {
    String encrypt = encryptRsa(dto.getPlain(), dto.getRsa());

    return RestUtil.ok(EncryptResponse.builder().encrypt(encrypt).build());
  }

  /**
   * 로그인
   *
   * @param dto {@link SignInRequest}
   * @return response entity
   * @apiNote 로그인
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @Override
  @Transactional
  public ResponseEntity<?> signIn(SignInRequest dto) {
    Long rsaTtl = entityData.getRsaTtl();
    String encodePublicKey = dto.getRsa();

    // RSA 유효 기간 체크
    if (!rsaPairUnit.checkRsa(encodePublicKey, rsaTtl)) {
      return RestUtil.error(RestUtil.getExceptions().getAccessDenied());
    }

    RsaPair redis = rsaPairUnit.get(encodePublicKey);
    String encodePrivateKey = redis.getPrivateKey();
    String username = decryptRsa(dto.getUsername(), encodePrivateKey);

    // 요청 정보로 데이터가 있는지 체크
    if (!accountAuthUnit.existsByUsername(username)) {
      return RestUtil.error(RestUtil.getExceptions().getEntityNotFound());
    }

    Account entity = accountAuthUnit.getByUsername(username);

    // 탈퇴 여부 체크
    if (entity.getDeleteFlag()) {
      return RestUtil.error(RestUtil.getExceptions().getEntityNotFound());
    }

    // 활성 여부 체크
    if (!entity.getUseFlag()) {
      return RestUtil.error(RestUtil.getExceptions().getUnAuthenticated());
    }

    /// 비밀번호 체크
    if (!passwordEncoder
        .matches(decryptRsa(dto.getPassword(), encodePrivateKey), entity.getPassword())) {
      return RestUtil.error(RestUtil.getExceptions().getUnAuthenticated());
    }

    entity.signed();

    String id = entity.getId();

    // 토큰 발급
    String accessToken = provider.generateAccessToken(id);
    String refreshToken = provider.generateRefreshToken(id);

    // 토큰 정보를 Redis 에 저장
    accessRedisUnit.save(AccessRedis.builder()
        .id(accessToken)
        .signId(id)
        .role(entity.getPrivilege().getRole())
        .build());
    refreshRedisUnit.save(RefreshRedis.builder()
        .id(refreshToken)
        .access(accessRedisUnit.get(accessToken))
        .build());

    SignInResponse response = SignInResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

    rsaPairUnit.delete(encodePublicKey);

    return RestUtil.ok(response);
  }

  /**
   * 로그아웃
   *
   * @return response entity
   * @apiNote 로그아웃
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @Override
  @Transactional
  public ResponseEntity<?> signOut() {
    String signedId = RestUtil.getSignedId();

    AccessRedis accessRedis = accessRedisUnit.getBySignId(signedId);

    refreshRedisUnit.delete(accessRedis);
    accessRedisUnit.deleteBySignId(signedId);

    return RestUtil.ok();
  }

  /**
   * 내 정보 조회
   *
   * @return info
   * @apiNote 내 정보 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @Override
  public ResponseEntity<?> getInfo() {
    return RestUtil.ok(map(RestUtil.getSigned(), AccountResponse.class));
  }

  /**
   * 비밀번호 변경
   *
   * @param dto {@link SignChangePasswordRequest}
   * @return response entity
   * @apiNote 비밀번호 변경
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @Override
  @Transactional
  public ResponseEntity<?> changePassword(SignChangePasswordRequest dto) {
    Long rsaTtl = entityData.getRsaTtl();
    String encodePublicKey = dto.getRsa();

    // RSA 유효 기간 체크
    if (!rsaPairUnit.checkRsa(encodePublicKey, rsaTtl)) {
      return RestUtil.error(RestUtil.getExceptions().getAccessDenied());
    }

    RsaPair redis = rsaPairUnit.get(encodePublicKey);
    String encodePrivateKey = redis.getPrivateKey();
    Account entity = accountAuthUnit.get(RestUtil.getSignedId());
    ExceptionData unAuthenticated = RestUtil.getExceptions().getUnAuthenticated();

    // 변경 전 비밀번호 체크
    if (!passwordEncoder
        .matches(decryptRsa(dto.getOriginPassword(), encodePrivateKey), entity.getPassword())) {
      return RestUtil.error(unAuthenticated);
    }

    String password = decryptRsa(dto.getPassword(), encodePrivateKey);

    // 변경할 비밀번호를 현재, 이전에 사용한 적 있는지 체크
    if (passwordEncoder.matches(password, entity.getPassword())) {
      return RestUtil.error(unAuthenticated, unAuthenticated.getMessage(), "CP001");
    }

    if (passwordEncoder.matches(password, entity.getPreviousPassword())) {
      return RestUtil.error(unAuthenticated, unAuthenticated.getMessage(), "CP002");
    }

    entity.changePassword(passwordEncoder.encode(password));

    return RestUtil.ok();
  }

  /**
   * 내 정보 수정
   *
   * @param dto {@link SignUpdateRequest}
   * @return response entity
   * @apiNote 내 정보 수정
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @Override
  @Transactional
  public ResponseEntity<?> updateInfo(SignUpdateRequest dto) {
    Long rsaTtl = entityData.getRsaTtl();
    String encodePublicKey = dto.getRsa();

    // RSA 유효 기간 체크
    if (!rsaPairUnit.checkRsa(encodePublicKey, rsaTtl)) {
      return RestUtil.error(RestUtil.getExceptions().getAccessDenied());
    }

    RsaPair redis = rsaPairUnit.get(encodePublicKey);
    String encodePrivateKey = redis.getPrivateKey();
    Account signed = RestUtil.getSigned();

    signed.updateEntity(dto.getGender(), decryptRsa(dto.getNickname(), encodePrivateKey));

    return RestUtil.ok();
  }

  /**
   * 탈퇴
   *
   * @return response entity
   * @apiNote 탈퇴
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @Override
  @Transactional
  public ResponseEntity<?> withdrawal() {
    Account signed = RestUtil.getSigned();
    String id = signed.getId();

    signed.withdrawal();

    refreshRedisUnit.delete(accessRedisUnit.getBySignId(id));
    accessRedisUnit.deleteBySignId(id);

    return RestUtil.ok();
  }

  /**
   * Access 토큰 갱신
   *
   * @param request 요청 정보
   * @param dto     {@link RefreshTokenRequest}
   * @return response entity
   * @apiNote Access 토큰 갱신
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @Override
  @Transactional
  public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, RefreshTokenRequest dto) {
    String refreshToken = provider.extractToken(request);
    String accessToken = dto.getAccessToken();

    provider.validateRefreshToken(refreshToken);

    RefreshRedis refresh = refreshRedisUnit.get(refreshToken); // Refresh Token 상세 조회

    /*
     * Refresh 토큰과 pairing 된 Access 토큰 정보와 요청 Access 토큰 정보가 다른지 체크
     * 두 정보가 다르다면 요청 Refresh 토큰은 제 3 자에게 탈취당한 것으로 판단
     * Refresh 토큰과 모든 Access 토큰을 파기
     * 모든 디바이스에서 로그아웃 처리
     */
    if (!refresh.getAccess().getId().equals(accessToken)) {
      accessRedisUnit.delete(accessToken);
      accessRedisUnit.delete(refresh.getAccess().getId());
      refreshRedisUnit.delete(refreshToken);

      return RestUtil.error(RestUtil.getExceptions().getUnAuthenticated());
    }

    LocalDateTime updateAt = refresh.getUpdateAt(); // Access Token 갱신 날짜 시간 조회
    AccessRedis access = accessRedisUnit.get(accessToken); // Access Token 상세 조회
    String id = access.getSignId(); // 계정 일련 번호 조회
    Role role = access.getRole(); // 계정 권한 조회

    // 인증인가 유지 기간을 넘었는지 확인. 넘었다면 로그아웃 처리
    long limit = entityData.getRefreshTtl();

    if (Duration.between(updateAt, LocalDateTime.now()).getSeconds() > limit) {
      accessRedisUnit.delete(accessToken);
      refreshRedisUnit.delete(refreshToken);

      return RestUtil.error(RestUtil.getExceptions().getUnAuthenticated());
    }

    // 새로운 Access Token 발급
    String newAccessToken = provider.generateAccessToken(id);

    accessRedisUnit.delete(accessToken);
    accessRedisUnit.save(AccessRedis.builder()
        .id(newAccessToken)
        .signId(id)
        .role(role)
        .build());

    refresh.updateRedis(accessRedisUnit.get(newAccessToken), limit);
    refreshRedisUnit.save(refresh);

    // 계정 최근 접속 날짜 시간 갱신
    accountAuthUnit.get(id).signed();

    RefreshTokenResponse response = RefreshTokenResponse
        .builder()
        .accessToken(newAccessToken)
        .build();

    return RestUtil.ok(response);
  }

}
