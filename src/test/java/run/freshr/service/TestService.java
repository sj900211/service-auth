package run.freshr.service;

import org.springframework.boot.ApplicationRunner;
import run.freshr.common.enumerations.Gender;
import run.freshr.domain.account.entity.Account;
import run.freshr.domain.auth.enumerations.Privilege;
import run.freshr.domain.auth.enumerations.Role;
import run.freshr.domain.auth.redis.AccessRedis;
import run.freshr.domain.auth.redis.RefreshRedis;

/**
 * 테스트 데이터 관리 service
 *
 * @author FreshR
 * @apiNote {@link ApplicationRunner} 를 상속받은 test runner 에서<br>
 *          데이터 설정을 쉽게하기 위해서 공통 데이터 생성 기능을 재정의<br>
 *          필수 작성은 아니며, 테스트 코드에서 데이터 생성 기능을 조금이라도 더 편하게 사용하고자 만든 Service<br>
 *          권한과 같은 특수한 경우를 제외한 대부분은 데이터에 대한 Create, Get 정도만 작성해서 사용을 한다.
 * @since 2024. 4. 2. 오후 2:15:54
 */
public interface TestService extends TestSecurityServiceAware {

  //      ___      __    __  .___________. __    __
  //     /   \    |  |  |  | |           ||  |  |  |
  //    /  ^  \   |  |  |  | `---|  |----`|  |__|  |
  //   /  /_\  \  |  |  |  |     |  |     |   __   |
  //  /  _____  \ |  `--'  |     |  |     |  |  |  |
  // /__/     \__\ \______/      |__|     |__|  |__|

  /**
   * 접근 토큰 생성
   *
   * @param accessToken access token
   * @param id          id
   * @param role        role
   * @apiNote 접근 토큰 생성
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:54
   */
  void createAccess(String accessToken, String id, Role role);

  /**
   * 접근 토큰 조회
   *
   * @param id id
   * @return access
   * @apiNote 접근 토큰 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:54
   */
  AccessRedis getAccess(String id);

  /**
   * 갱신 토큰 생성
   *
   * @param refreshToken refresh token
   * @param access       access
   * @apiNote 갱신 토큰 생성
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:54
   */
  void createRefresh(String refreshToken, String access);

  /**
   * 갱신 토큰 조회
   *
   * @param refreshToken refresh token
   * @return refresh
   * @apiNote 갱신 토큰 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:54
   */
  RefreshRedis getRefresh(String refreshToken);

  //      ___       ______   ______   ______    __    __  .__   __. .___________.
  //     /   \     /      | /      | /  __  \  |  |  |  | |  \ |  | |           |
  //    /  ^  \   |  ,----'|  ,----'|  |  |  | |  |  |  | |   \|  | `---|  |----`
  //   /  /_\  \  |  |     |  |     |  |  |  | |  |  |  | |  . `  |     |  |
  //  /  _____  \ |  `----.|  `----.|  `--'  | |  `--'  | |  |\   |     |  |
  // /__/     \__\ \______| \______| \______/   \______/  |__| \__|     |__|

  /**
   * 사용자 관리 > 계정 생성
   *
   * @param prefix prefix
   * @param suffix suffix
   * @return string
   * @apiNote 사용자 관리 > 계정 생성
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:54
   */
  String createAccount(String prefix, String suffix, Gender gender, Privilege privilege);

  /**
   * 사용자 관리 > 계정 조회
   *
   * @param id id
   * @return account
   * @apiNote 사용자 관리 > 계정 조회
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:15:55
   */
  Account getAccount(String id);

}
