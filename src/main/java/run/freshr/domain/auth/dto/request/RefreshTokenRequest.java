package run.freshr.domain.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Access 토큰 갱신 request DTO
 *
 * @author FreshR
 * @apiNote Access 토큰 갱신 request DTO
 * @since 2024. 4. 2. 오후 1:06:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

  /**
   * Access 토큰
   *
   * @apiNote Access 토큰
   * @since 2024. 4. 2. 오후 1:06:17
   */
  @NotEmpty
  private String accessToken;

}
