package run.freshr.controller;

import static java.lang.System.lineSeparator;
import static java.nio.file.Files.readAllLines;
import static java.util.stream.Collectors.joining;
import static run.freshr.common.utils.RestUtil.getConfig;
import static run.freshr.domain.auth.enumerations.Role.Secured.ANONYMOUS;
import static run.freshr.domain.auth.enumerations.Role.Secured.MANAGER_MAJOR;
import static run.freshr.domain.auth.enumerations.Role.Secured.MANAGER_MINOR;
import static run.freshr.domain.auth.enumerations.Role.Secured.USER;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import run.freshr.common.configurations.URIConfiguration;
import run.freshr.common.mappers.EnumMapper;
import run.freshr.common.utils.RestUtil;

/**
 * 공통 관리 controller
 *
 * @author FreshR
 * @apiNote 공통 관리 controller
 * @since 2024. 4. 2. 오전 11:27:50
 */
@RestController
@RequiredArgsConstructor
public class CommonController {

  private final EnumMapper enumMapper;

  /**
   * Health check
   *
   * @return heart beat
   * @throws IOException io exception
   * @apiNote Health check
   * @author FreshR
   * @since 2024. 4. 2. 오전 11:27:50
   */
  @GetMapping(URIConfiguration.uriCommonHeartbeat)
  public String getHeartBeat() throws IOException {
    return readAllLines(getConfig().getHeartbeat().getFile().toPath())
        .stream()
        .collect(joining(lineSeparator()));
  }

  //  _______ .__   __.  __    __  .___  ___.
  // |   ____||  \ |  | |  |  |  | |   \/   |
  // |  |__   |   \|  | |  |  |  | |  \  /  |
  // |   __|  |  . `  | |  |  |  | |  |\/|  |
  // |  |____ |  |\   | |  `--'  | |  |  |  |
  // |_______||__| \__|  \______/  |__|  |__|

  /**
   * 열거형 Data 조회 - All
   *
   * @return enum list
   * @apiNote 열거형 Data 조회 - All
   * @author FreshR
   * @since 2024. 4. 2. 오전 11:27:50
   */
  @Secured({MANAGER_MAJOR, MANAGER_MINOR, USER, ANONYMOUS})
  @GetMapping(URIConfiguration.uriCommonEnum)
  public ResponseEntity<?> getEnumList() {
    return RestUtil.ok(enumMapper.getAll());
  }

  /**
   * 열거형 Data 조회 - One To Many
   *
   * @param pick KEY 값
   * @return enum
   * @apiNote 열거형 Data 조회 - One To Many
   * @author FreshR
   * @since 2024. 4. 2. 오전 11:27:50
   */
  @Secured({MANAGER_MAJOR, MANAGER_MINOR, USER, ANONYMOUS})
  @GetMapping(URIConfiguration.uriCommonEnumPick)
  public ResponseEntity<?> getEnum(@PathVariable String pick) {
    return RestUtil.ok(enumMapper.get(pick.toLowerCase()));
  }

}
