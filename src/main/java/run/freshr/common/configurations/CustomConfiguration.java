package run.freshr.common.configurations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 추가 설정
 *
 * @author FreshR
 * @apiNote 추가 설정
 * @since 2024. 4. 2. 오전 9:59:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Configuration
@EnableConfigurationProperties
public class CustomConfiguration extends CustomConfigurationAware {

}
