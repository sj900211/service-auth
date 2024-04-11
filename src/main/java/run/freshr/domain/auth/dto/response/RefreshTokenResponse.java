package run.freshr.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Access 토큰 갱신 response DTO
 *
 * @author FreshR
 * @apiNote Access 토큰 갱신 response DTO
 * @since 2024. 4. 2. 오후 1:06:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {

  /**
   * Access 토큰
   *
   * @apiNote Access 토큰
   * @since 2024. 4. 2. 오후 1:06:17
   */
  private String accessToken;

}
