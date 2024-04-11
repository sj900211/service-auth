package run.freshr.controller;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static java.lang.System.lineSeparator;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static run.freshr.domain.auth.enumerations.Role.ROLE_ANONYMOUS;
import static run.freshr.domain.auth.enumerations.Role.ROLE_USER;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import run.freshr.common.configurations.URIConfiguration;
import run.freshr.common.enumerations.Gender;
import run.freshr.common.extensions.TestExtension;
import run.freshr.domain.auth.AuthDocs;
import run.freshr.domain.common.EnumDocs;

@DisplayName("공통 관리")
class CommonControllerTest extends TestExtension {

  //  _______ .__   __.  __    __  .___  ___.
  // |   ____||  \ |  | |  |  |  | |   \/   |
  // |  |__   |   \|  | |  |  |  | |  \  /  |
  // |   __|  |  . `  | |  |  |  | |  |\/|  |
  // |  |____ |  |\   | |  `--'  | |  |  |  |
  // |_______||__| \__|  \______/  |__|  |__|

  @Test
  @DisplayName("열거형 Data 조회 - All")
  public void getEnumList() throws Exception {
    setSignedUser();

    GET(URIConfiguration.uriCommonEnum)
        .andDo(print())
        .andDo(docs(ResourceSnippetParameters
            .builder()
            .summary("열거형 Data 조회 - All")
            .description(AuthDocs.Data.descriptionAuthorizationAccess()
                + lineSeparator()
                + AuthDocs.Data.descriptionRole(ROLE_USER, ROLE_ANONYMOUS))
            .build()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("열거형 Data 조회 - One To Many")
  public void getEnum() throws Exception {
    setSignedUser();

    GET(URIConfiguration.uriCommonEnumPick,
        UPPER_CAMEL.to(LOWER_HYPHEN, Gender.class.getSimpleName()).toLowerCase())
        .andDo(print())
        .andDo(docs(ResourceSnippetParameters
            .builder()
            .summary("열거형 Data 조회 - One To Many")
            .description(AuthDocs.Data.descriptionAuthorizationAccess()
                + lineSeparator()
                + AuthDocs.Data.descriptionRole(ROLE_USER, ROLE_ANONYMOUS))
            .pathParameters(EnumDocs.Request.getEnum())
            .build()))
        .andExpect(status().isOk());
  }

}
