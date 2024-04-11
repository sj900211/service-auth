package run.freshr.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import run.freshr.domain.auth.dto.request.EncryptRequest;
import run.freshr.domain.auth.dto.request.RefreshTokenRequest;
import run.freshr.domain.auth.dto.request.SignChangePasswordRequest;
import run.freshr.domain.auth.dto.request.SignInRequest;
import run.freshr.domain.auth.dto.request.SignUpdateRequest;

/**
 * 권한 관리 service
 *
 * @author FreshR
 * @apiNote 권한 관리 service
 * @since 2024. 4. 2. 오후 1:06:17
 */
public interface AuthService {

  /**
   * RSA 공개키 조회
   *
   * @return public key
   * @apiNote RSA 공개키 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  ResponseEntity<?> getPublicKey();

  /**
   * RSA 암호화 조회
   *
   * @param dto {@link EncryptRequest}
   * @return encrypt rsa
   * @apiNote RSA 암호화 조회<br>
   *          사용하지 않는 것을 권장<br>
   *          RSA 암호화를 할 수 없는 플랫폼일 때 사용
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  ResponseEntity<?> getEncryptRsa(EncryptRequest dto);

  /**
   * 로그인
   *
   * @param dto {@link SignInRequest}
   * @return response entity
   * @apiNote 로그인
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  ResponseEntity<?> signIn(SignInRequest dto);

  /**
   * 로그아웃
   *
   * @return response entity
   * @apiNote 로그아웃
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  ResponseEntity<?> signOut();

  /**
   * 내 정보 조회
   *
   * @return info
   * @apiNote 내 정보 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  ResponseEntity<?> getInfo();

  /**
   * 비밀번호 변경
   *
   * @param dto {@link SignChangePasswordRequest}
   * @return response entity
   * @apiNote 비밀번호 변경
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  ResponseEntity<?> changePassword(SignChangePasswordRequest dto);

  /**
   * 내 정보 수정
   *
   * @param dto {@link SignUpdateRequest}
   * @return response entity
   * @apiNote 내 정보 수정
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  ResponseEntity<?> updateInfo(SignUpdateRequest dto);

  /**
   * 탈퇴
   *
   * @return response entity
   * @apiNote 탈퇴
   * @author FreshR
   * @since 2024. 4. 2. 오후 1:06:17
   */
  ResponseEntity<?> withdrawal();

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
  ResponseEntity<?> refreshAccessToken(HttpServletRequest request, RefreshTokenRequest dto);

}
