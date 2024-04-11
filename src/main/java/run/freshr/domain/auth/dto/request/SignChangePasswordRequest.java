package run.freshr.domain.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 비밀번호 변경 request DTO
 *
 * @author FreshR
 * @apiNote 비밀번호 변경 request DTO
 * @since 2024. 4. 2. 오후 1:06:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignChangePasswordRequest {

  /**
   * RSA 공개 키
   *
   * @apiNote RSA 공개 키
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @NotEmpty
  private String rsa;

  /**
   * 변경 전 비밀번호
   *
   * @apiNote 변경 전 비밀번호
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @NotEmpty
  private String originPassword;

  /**
   * 변경 비밀번호
   *
   * @apiNote 변경 비밀번호
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @NotEmpty
  private String password;

}
