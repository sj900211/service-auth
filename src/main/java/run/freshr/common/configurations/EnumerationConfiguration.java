package run.freshr.common.configurations;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static java.util.List.of;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import run.freshr.common.enumerations.Gender;
import run.freshr.common.mappers.EnumMapper;
import run.freshr.common.mappers.EnumModel;

/**
 * Enum 설정
 *
 * @author FreshR
 * @apiNote API 로 Enum 데이터를 읽을 수 있도록 설정
 * @since 2024. 4. 2. 오전 10:01:07
 */
@Configuration
public class EnumerationConfiguration {

  /**
   * API 로 접근 가능한 Enum 데이터 설정
   *
   * @apiNote 여기에 설정한 데이터만 API 를 통해서 접근 가능
   * @since 2024. 4. 2. 오전 10:01:07
   */
  private final List<Class<? extends EnumModel>> enums = new ArrayList<>(of(
      Gender.class
  ));

  /**
   * EnumMapper Bean 등록
   *
   * @return enum mapper
   * @apiNote 설정한 목록만 접근 가능하도록 Bean 등록
   * @author FreshR
   * @since 2024. 4. 2. 오전 10:01:07
   */
  @Bean
  public EnumMapper enumMapper() {
    return setMapper(enums);
  }

  /**
   * Enum 데이터 가공
   *
   * @param classes classes
   * @return mapper
   * @apiNote Enum 데이터를 API 에서 접근할 때<br>
   *          Class 이름을 lower-hyphen 으로 변경한 뒤 Key 값으로 활용
   * @author FreshR
   * @since 2024. 4. 2. 오전 10:01:07
   */
  private EnumMapper setMapper(List<Class<? extends EnumModel>> classes) {
    EnumMapper enumMapper = new EnumMapper();

    classes.forEach(enumClass -> enumMapper
        .put(UPPER_CAMEL.to(LOWER_HYPHEN, enumClass.getSimpleName()), enumClass));

    return enumMapper;
  }

}
