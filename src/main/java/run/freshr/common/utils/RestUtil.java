package run.freshr.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import run.freshr.common.configurations.CustomConfiguration;
import run.freshr.common.data.ExceptionsData;
import run.freshr.domain.auth.unit.jpa.AccountAuthUnit;

/**
 * 자주 사용하는 공통 기능을 정의
 *
 * @author FreshR
 * @apiNote 자주 사용하는 공통 기능을 정의<br>
 *          {@link RestUtilSecurityAware} 를 상속 받아 사용
 * @since 2024. 4. 2. 오전 10:03:46
 */
@Component
public class RestUtil extends RestUtilSecurityAware {

  private static CustomConfiguration customConfiguration;

  @Autowired
  public RestUtil(Environment environment, ExceptionsData exceptionsData,
      AccountAuthUnit accountAuthUnit, CustomConfiguration customConfiguration) {
    super(environment, exceptionsData, accountAuthUnit);

    RestUtil.customConfiguration = customConfiguration;
  }

  public static CustomConfiguration getConfig() {
    return customConfiguration;
  }

}
