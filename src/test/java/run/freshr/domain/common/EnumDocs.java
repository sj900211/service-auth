package run.freshr.domain.common;

import org.springframework.restdocs.request.ParameterDescriptor;
import run.freshr.common.utils.PrintUtil;

public class EnumDocs {

  public static class Request {
    public static ParameterDescriptor[] getEnum() {
      return PrintUtil
          .builder()

          .parameter("pick", "그룹 이름")

          .build()
          .getParameters();
    }
  }

  public static class Response {
  }

  public static class Data {
  }

}
