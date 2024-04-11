package run.freshr.domain.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import run.freshr.common.enumerations.Gender;

/**
 * 내 정보 수정 request DTO
 *
 * @author FreshR
 * @apiNote 내 정보 수정 request DTO
 * @since 2024. 4. 2. 오후 1:06:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpdateRequest {

  /**
   * RSA 공개 키
   *
   * @apiNote RSA 공개 키
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @NotEmpty
  private String rsa;

  @NotEmpty
  private String nickname;

  @NotNull
  private Gender gender;

}
