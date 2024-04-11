package run.freshr.domain.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 로그인 request DTO
 *
 * @author FreshR
 * @apiNote 로그인 request DTO
 * @since 2024. 4. 2. 오후 1:06:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

  /**
   * RSA 공개 키
   *
   * @apiNote RSA 공개 키
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @NotEmpty
  private String rsa;

  /**
   * 계정 고유 아이디
   *
   * @apiNote 계정 고유 아이디
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @NotEmpty
  private String username;

  /**
   * 비밀번호
   *
   * @apiNote 비밀번호
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @NotEmpty
  private String password;

}
