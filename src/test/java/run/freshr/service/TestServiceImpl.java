package run.freshr.service;

import static run.freshr.common.utils.ThreadUtil.threadAccess;
import static run.freshr.common.utils.ThreadUtil.threadPublicKey;
import static run.freshr.common.utils.ThreadUtil.threadRefresh;
import static run.freshr.domain.account.enumerations.AccountStatus.ACTIVE;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import run.freshr.common.enumerations.Gender;
import run.freshr.common.security.TokenProvider;
import run.freshr.common.utils.CryptoUtil;
import run.freshr.domain.account.entity.Account;
import run.freshr.domain.auth.enumerations.Privilege;
import run.freshr.domain.auth.enumerations.Role;
import run.freshr.domain.auth.redis.AccessRedis;
import run.freshr.domain.auth.redis.RefreshRedis;
import run.freshr.domain.auth.redis.RsaPair;
import run.freshr.domain.auth.unit.jpa.AccountAuthUnit;
import run.freshr.domain.auth.unit.redis.AccessRedisUnit;
import run.freshr.domain.auth.unit.redis.RefreshRedisUnit;
import run.freshr.domain.auth.unit.redis.RsaPairUnit;

/**
 * 테스트 데이터 관리 service 구현 class
 *
 * @author FreshR
 * @apiNote 테스트 데이터 관리 service 구현 class
 * @since 2024. 4. 2. 오후 2:18:39
 */
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

  //      ___      __    __  .___________. __    __
  //     /   \    |  |  |  | |           ||  |  |  |
  //    /  ^  \   |  |  |  | `---|  |----`|  |__|  |
  //   /  /_\  \  |  |  |  |     |  |     |   __   |
  //  /  _____  \ |  `--'  |     |  |     |  |  |  |
  // /__/     \__\ \______/      |__|     |__|  |__|
  private final RsaPairUnit rsaPairUnit;
  private final AccessRedisUnit authAccessUnit;
  private final RefreshRedisUnit authRefreshUnit;

  private final TokenProvider provider;

  @Override
  public void createRsa() {
    KeyPair keyPar = CryptoUtil.getKeyPar();
    PublicKey publicKey = keyPar.getPublic();
    PrivateKey privateKey = keyPar.getPrivate();
    String encodePublicKey = CryptoUtil.encodePublicKey(publicKey);
    String encodePrivateKey = CryptoUtil.encodePrivateKey(privateKey);

    threadPublicKey.set(encodePublicKey);

    rsaPairUnit.save(RsaPair.builder()
        .publicKey(encodePublicKey)
        .privateKey(encodePrivateKey)
        .createAt(LocalDateTime.now())
        .build());
  }

  @Override
  public void createAuth(String id, Role role) {
    // 토큰 발급
    String accessToken = provider.generateAccessToken(id);
    String refreshToken = provider.generateRefreshToken(id);

    threadAccess.set(accessToken);
    threadRefresh.set(refreshToken);

    // 토큰 등록
    createAccess(accessToken, id, role);
    createRefresh(refreshToken, accessToken);
  }

  /**
   * 접근 토큰 생성
   *
   * @param accessToken access token
   * @param id          id
   * @param role        role
   * @apiNote 접근 토큰 생성
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:54
   */
  @Override
  public void createAccess(String accessToken, String id, Role role) {
    AccessRedis accessRedis = AccessRedis
        .builder()
        .id(accessToken)
        .signId(id)
        .role(role)
        .build();

    authAccessUnit.save(accessRedis);
  }

  /**
   * 접근 토큰 조회
   *
   * @param id id
   * @return access
   * @apiNote 접근 토큰 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:54
   */
  @Override
  public AccessRedis getAccess(String id) {
    return authAccessUnit.get(id);
  }

  /**
   * 갱신 토큰 생성
   *
   * @param refreshToken refresh token
   * @param access       access
   * @apiNote 갱신 토큰 생성
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:54
   */
  @Override
  public void createRefresh(String refreshToken, String access) {
    RefreshRedis refreshRedis = RefreshRedis
        .builder()
        .id(refreshToken)
        .access(getAccess(access))
        .build();

    authRefreshUnit.save(refreshRedis);
  }

  /**
   * 갱신 토큰 조회
   *
   * @param refreshToken refresh token
   * @return refresh
   * @apiNote 갱신 토큰 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:54
   */
  @Override
  public RefreshRedis getRefresh(String refreshToken) {
    return authRefreshUnit.get(refreshToken);
  }

  //      ___       ______   ______   ______    __    __  .__   __. .___________.
  //     /   \     /      | /      | /  __  \  |  |  |  | |  \ |  | |           |
  //    /  ^  \   |  ,----'|  ,----'|  |  |  | |  |  |  | |   \|  | `---|  |----`
  //   /  /_\  \  |  |     |  |     |  |  |  | |  |  |  | |  . `  |     |  |
  //  /  _____  \ |  `----.|  `----.|  `--'  | |  `--'  | |  |\   |     |  |
  // /__/     \__\ \______| \______| \______/   \______/  |__| \__|     |__|

  private final AccountAuthUnit accountAuthUnit;

  private final PasswordEncoder passwordEncoder;

  /**
   * 사용자 관리 > 계정 생성
   *
   * @param prefix prefix
   * @param suffix suffix
   * @return string
   * @apiNote 사용자 관리 > 계정 생성
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:54
   */
  @Override
  public String createAccount(String prefix, String suffix, Gender gender, Privilege privilege) {
    Account entity = Account
        .builder()
        .id("A-AI-" + suffix)
        .privilege(privilege)
        .status(ACTIVE)
        .gender(gender)
        .username(prefix + suffix + "@freshr.run")
        .nickname(prefix + suffix)
        .password(passwordEncoder.encode("1234"))
        .build();

    return accountAuthUnit.create(entity);
  }

  /**
   * 사용자 관리 > 계정 조회
   *
   * @param id id
   * @return account
   * @apiNote 사용자 관리 > 계정 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:55
   */
  @Override
  public Account getAccount(String id) {
    return accountAuthUnit.get(id);
  }

}
