package run.freshr.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RSA 암호화 조회 response DTO
 *
 * @author FreshR
 * @apiNote RSA 암호화 조회 response DTO
 * @since 2024. 4. 2. 오후 1:06:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncryptResponse {

  /**
   * 암호화된 문자
   *
   * @apiNote 암호화된 문자
   * @since 2024. 4. 2. 오후 1:06:17
   */
  private String encrypt;

}
