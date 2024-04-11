package run.freshr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {

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
    log.info("-------------------------------------------------------------------");
  }

}
