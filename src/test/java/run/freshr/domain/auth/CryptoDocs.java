package run.freshr.domain.auth;

import static org.springframework.restdocs.payload.JsonFieldType.STRING;

import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;
import run.freshr.common.docs.ResponseDocs;

public class CryptoDocs {

  public static class Request {
  }

  public static class Response {
    public static List<FieldDescriptor> getPublicKey() {
      return ResponseDocs
          .data()

          .field("key", "BASE64 로 인코딩된 RSA 공개키", STRING)

          .build()
          .getFieldList();
    }
  }

  public static class Docs {
  }

}
