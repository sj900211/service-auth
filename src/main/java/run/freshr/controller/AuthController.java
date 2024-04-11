package run.freshr.controller;

import static run.freshr.domain.auth.enumerations.Role.Secured.ANONYMOUS;
import static run.freshr.domain.auth.enumerations.Role.Secured.MANAGER_MAJOR;
import static run.freshr.domain.auth.enumerations.Role.Secured.MANAGER_MINOR;
import static run.freshr.domain.auth.enumerations.Role.Secured.USER;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import run.freshr.common.configurations.URIConfiguration;
import run.freshr.domain.auth.dto.request.EncryptRequest;
import run.freshr.domain.auth.dto.request.RefreshTokenRequest;
import run.freshr.domain.auth.dto.request.SignChangePasswordRequest;
import run.freshr.domain.auth.dto.request.SignInRequest;
import run.freshr.domain.auth.dto.request.SignUpdateRequest;
import run.freshr.service.AuthService;

/**
 * 권한 관리 controller
 *
 * @author FreshR
 * @apiNote 권한 관리 controller
 * @since 2024. 4. 2. 오후 1:26:55
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService service;

  /**
   * RSA 공개키 조회
   *
   * @return public key
   * @apiNote RSA 공개키 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:26:55
   */
  @Secured({MANAGER_MAJOR, MANAGER_MINOR, USER, ANONYMOUS})
  @GetMapping(URIConfiguration.uriAuthCrypto)
  public ResponseEntity<?> getPublicKey() {
    return service.getPublicKey();
  }

  /**
   * RSA 암호화 조회
   *
   * @param dto {@link EncryptRequest}
   * @return encrypt rsa
   * @apiNote RSA 암호화 조회<br>
   *          사용하지 않는 것을 권장<br>
   *          RSA 암호화를 할 수 없는 플랫폼일 때 사용
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:26:55
   */
  @Secured({MANAGER_MAJOR, MANAGER_MINOR, USER, ANONYMOUS})
  @PostMapping(URIConfiguration.uriAuthCrypto)
  public ResponseEntity<?> getEncryptRsa(@RequestBody @Valid EncryptRequest dto) {
    return service.getEncryptRsa(dto);
  }

  /**
   * 로그인
   *
   * @param dto {@link SignInRequest}
   * @return response entity
   * @apiNote 로그인
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:26:55
   */
  @Secured(ANONYMOUS)
  @PostMapping(URIConfiguration.uriAuthSignIn)
  public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest dto) {
    return service.signIn(dto);
  }

  /**
   * 로그아웃
   *
   * @return response entity
   * @apiNote 로그아웃
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:26:55
   */
  @Secured({MANAGER_MAJOR, MANAGER_MINOR, USER})
  @PostMapping(URIConfiguration.uriAuthSignOut)
  public ResponseEntity<?> signOut() {
    return service.signOut();
  }

  /**
   * 내 정보 조회
   *
   * @return info
   * @apiNote 내 정보 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:26:55
   */
  @Secured({MANAGER_MAJOR, MANAGER_MINOR, USER})
  @GetMapping(URIConfiguration.uriAuthInfo)
  public ResponseEntity<?> getInfo() {
    return service.getInfo();
  }

  /**
   * 비밀번호 변경
   *
   * @param dto {@link SignChangePasswordRequest}
   * @return response entity
   * @apiNote 비밀번호 변경
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:26:55
   */
  @Secured({MANAGER_MAJOR, MANAGER_MINOR, USER})
  @PutMapping(URIConfiguration.uriAuthPassword)
  public ResponseEntity<?> changePassword(@RequestBody @Valid SignChangePasswordRequest dto) {
    return service.changePassword(dto);
  }

  /**
   * 내 정보 수정
   *
   * @param dto {@link SignUpdateRequest}
   * @return response entity
   * @apiNote 내 정보 수정
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:26:55
   */
  @Secured({MANAGER_MAJOR, MANAGER_MINOR, USER})
  @PutMapping(URIConfiguration.uriAuthInfo)
  public ResponseEntity<?> updateInfo(@RequestBody @Valid SignUpdateRequest dto) {
    return service.updateInfo(dto);
  }

  /**
   * 탈퇴
   *
   * @return response entity
   * @apiNote 탈퇴
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:26:55
   */
  @Secured({MANAGER_MAJOR, MANAGER_MINOR, USER})
  @DeleteMapping(URIConfiguration.uriAuthInfo)
  public ResponseEntity<?> withdrawal() {
    return service.withdrawal();
  }

  /**
   * Access 토큰 갱신
   *
   * @param request 요청 정보
   * @param dto     {@link RefreshTokenRequest}
   * @return response entity
   * @apiNote Access 토큰 갱신
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:26:55
   */
  @PostMapping(URIConfiguration.uriAuthRefresh)
  public ResponseEntity<?> refreshAccessToken(HttpServletRequest request,
      @RequestBody @Valid RefreshTokenRequest dto) {
    return service.refreshAccessToken(request, dto);
  }

}
