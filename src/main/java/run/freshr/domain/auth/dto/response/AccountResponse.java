package run.freshr.domain.auth.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import run.freshr.common.enumerations.Gender;
import run.freshr.common.extensions.response.ResponseLogicalExtension;

/**
 * 사용자 계정 response DTO
 *
 * @author FreshR
 * @apiNote 사용자 계정 response DTO
 * @since 2024. 4. 2. 오후 1:06:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountResponse extends ResponseLogicalExtension<String> {

  private String username;

  private String nickname;

  private Gender gender;

  private LocalDateTime signAt;

}
