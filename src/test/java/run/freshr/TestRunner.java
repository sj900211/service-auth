package run.freshr;

import static run.freshr.common.enumerations.Gender.OTHERS;
import static run.freshr.common.utils.StringUtil.padding;
import static run.freshr.domain.auth.enumerations.Privilege.MANAGER_MAJOR;
import static run.freshr.domain.auth.enumerations.Privilege.MANAGER_MINOR;
import static run.freshr.domain.auth.enumerations.Privilege.USER;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import run.freshr.common.extensions.TestSecurityRunnerAware;
import run.freshr.service.TestService;

@Slf4j
@Component
@Profile("test")
public class TestRunner extends TestSecurityRunnerAware {

  public static List<String> userIdList = new ArrayList<>();
  public static List<String> managerIdList = new ArrayList<>();

  @Autowired
  private TestService service;

  @Override
  public void run(ApplicationArguments args) {
    log.info("-------------------------------------------------------------------");
    log.info(" _______ .______       _______     _______. __    __  .______");
    log.info("|   ____||   _  \\     |   ____|   /       ||  |  |  | |   _  \\");
    log.info("|  |__   |  |_)  |    |  |__     |   (----`|  |__|  | |  |_)  |");
    log.info("|   __|  |      /     |   __|     \\   \\    |   __   | |      /");
    log.info("|  |     |  |\\  \\----.|  |____.----)   |   |  |  |  | |  |\\  \\----.");
    log.info("|__|     | _| `._____||_______|_______/    |__|  |__| | _| `._____|");
    log.info("     _______. _______ .______     ____    ____  __    ______  _______");
    log.info("    /       ||   ____||   _  \\    \\   \\  /   / |  |  /      ||   ____|");
    log.info("   |   (----`|  |__   |  |_)  |    \\   \\/   /  |  | |  ,----'|  |__");
    log.info("    \\   \\    |   __|  |      /      \\      /   |  | |  |     |   __|");
    log.info(".----)   |   |  |____ |  |\\  \\----.  \\    /    |  | |  `----.|  |____");
    log.info("|_______/    |_______|| _| `._____|   \\__/     |__|  \\______||_______|");
    log.info("     ___      __    __  .___________. __    __");
    log.info("    /   \\    |  |  |  | |           ||  |  |  |");
    log.info("   /  ^  \\   |  |  |  | `---|  |----`|  |__|  |");
    log.info("  /  /_\\  \\  |  |  |  |     |  |     |   __   |");
    log.info(" /  _____  \\ |  `--'  |     |  |     |  |  |  |");
    log.info("/__/     \\__\\ \\______/      |__|     |__|  |__|");
    log.info(".___________. _______     _______.___________.");
    log.info("|           ||   ____|   /       |           |");
    log.info("`---|  |----`|  |__     |   (----`---|  |----`");
    log.info("    |  |     |   __|     \\   \\       |  |");
    log.info("    |  |     |  |____.----)   |      |  |");
    log.info("    |__|     |_______|_______/       |__|");
    log.info("-------------------------------------------------------------------");

    String mightyId = service.createAccount("mighty", "0", OTHERS, MANAGER_MAJOR);

    for (int i = 0; i < 15; i++) {
      String padding = padding(i, 3);
      String managerId = service.createAccount("manager", padding, OTHERS, MANAGER_MINOR);
      String userId = service.createAccount("user", padding, OTHERS, USER);

      TestRunner.userIdList.add(userId);
      TestRunner.managerIdList.add(managerId);

      if (i == 0) {
        TestRunner.managerId = managerId;
        TestRunner.userId = userId;
      }
    }

    TestRunner.mightyId = mightyId;
  }

}
