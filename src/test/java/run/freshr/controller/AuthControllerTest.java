package run.freshr.controller;

import static java.lang.System.lineSeparator;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static run.freshr.common.enumerations.Gender.OTHERS;
import static run.freshr.common.utils.CryptoUtil.encryptRsa;
import static run.freshr.common.utils.ThreadUtil.threadAccess;
import static run.freshr.common.utils.ThreadUtil.threadPublicKey;
import static run.freshr.common.utils.ThreadUtil.threadRefresh;
import static run.freshr.domain.auth.enumerations.Role.ROLE_ANONYMOUS;
import static run.freshr.domain.auth.enumerations.Role.ROLE_MANAGER_MAJOR;
import static run.freshr.domain.auth.enumerations.Role.ROLE_MANAGER_MINOR;
import static run.freshr.domain.auth.enumerations.Role.ROLE_USER;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import run.freshr.TestRunner;
import run.freshr.common.configurations.URIConfiguration;
import run.freshr.common.extensions.TestExtension;
import run.freshr.domain.auth.AuthDocs;
import run.freshr.domain.auth.CryptoDocs;
import run.freshr.domain.auth.dto.request.RefreshTokenRequest;
import run.freshr.domain.auth.dto.request.SignChangePasswordRequest;
import run.freshr.domain.auth.dto.request.SignInRequest;
import run.freshr.domain.auth.dto.request.SignUpdateRequest;

@DisplayName("권한 관리")
public class AuthControllerTest extends TestExtension {

  @Test
  @DisplayName("RSA 공개키 조회")
  public void getPublicKey() throws Exception {
    setSignedUser();

    GET(URIConfiguration.uriAuthCrypto)
        .andDo(print())
        .andDo(docs(ResourceSnippetParameters
            .builder()
            .summary("RSA 공개키 조회")
            .description(AuthDocs.Data.descriptionRole(
                ROLE_MANAGER_MAJOR, ROLE_MANAGER_MINOR, ROLE_USER, ROLE_ANONYMOUS))
            .responseFields(CryptoDocs.Response.getPublicKey())
            .build()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("로그인")
  public void signIn() throws Exception {
    setAnonymous();
    setRsa();

    apply();

    String publicKey = threadPublicKey.get();

    POST_BODY(
        URIConfiguration.uriAuthSignIn,
        SignInRequest
            .builder()
            .rsa(publicKey)
            .username(encryptRsa(service.getAccount(TestRunner.userId).getUsername(), publicKey))
            .password(encryptRsa("1234", publicKey))
            .build()
    ).andDo(print())
        .andDo(docs(ResourceSnippetParameters
            .builder()
            .summary("로그인")
            .description(AuthDocs.Data.descriptionRsa()
                + lineSeparator() + lineSeparator()
                + AuthDocs.Data.descriptionRole(ROLE_ANONYMOUS))
            .requestFields(AuthDocs.Request.signIn())
            .responseFields(AuthDocs.Response.signIn())
            .build()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("로그인 계정 정보 조회")
  public void getInfo() throws Exception {
    setSignedUser();

    apply();

    GET(URIConfiguration.uriAuthInfo)
        .andDo(print())
        .andDo(docs(ResourceSnippetParameters
            .builder()
            .summary("로그인 계정 정보 조회")
            .description(AuthDocs.Data.descriptionAuthorizationAccess()
                + lineSeparator()
                + AuthDocs.Data.descriptionRole(ROLE_MANAGER_MAJOR, ROLE_MANAGER_MINOR, ROLE_USER))
            .responseFields(AuthDocs.Response.getInfo())
            .build()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("비밀번호 변경")
  public void changePassword() throws Exception {
    setSignedUser();
    setRsa();

    apply();

    PUT_BODY(
        URIConfiguration.uriAuthPassword,
        SignChangePasswordRequest
            .builder()
            .rsa(threadPublicKey.get())
            .originPassword(encryptRsa("1234", threadPublicKey.get()))
            .password(encryptRsa("input password", threadPublicKey.get()))
            .build()
    ).andDo(print())
        .andDo(docs(ResourceSnippetParameters
            .builder()
            .summary("비밀번호 변경")
            .description(AuthDocs.Data.descriptionAuthorizationAccess()
                + lineSeparator() + lineSeparator()
                + AuthDocs.Data.descriptionRsa()
                + lineSeparator() + lineSeparator()
                + AuthDocs.Data.descriptionRole(ROLE_MANAGER_MAJOR, ROLE_MANAGER_MINOR, ROLE_USER))
            .requestFields(AuthDocs.Request.changePassword())
            .build()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("계정 정보 수정")
  public void updateInfo() throws Exception {
    setSignedUser();
    setRsa();

    apply();

    PUT_BODY(
        URIConfiguration.uriAuthInfo,
        SignUpdateRequest
            .builder()
            .rsa(threadPublicKey.get())
            .nickname(encryptRsa("input nickname", threadPublicKey.get()))
            .gender(OTHERS)
            .build()
    ).andDo(print())
        .andDo(docs(ResourceSnippetParameters
            .builder()
            .summary("계정 정보 수정")
            .description(AuthDocs.Data.descriptionAuthorizationAccess()
                + lineSeparator() + lineSeparator()
                + AuthDocs.Data.descriptionRsa()
                + lineSeparator() + lineSeparator()
                + AuthDocs.Data.descriptionRole(ROLE_MANAGER_MAJOR, ROLE_MANAGER_MINOR, ROLE_USER))
            .requestFields(AuthDocs.Request.updateInfo())
            .build()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("로그인한 계정 탈퇴 처리")
  public void withdrawal() throws Exception {
    setSignedUser();

    apply();

    DELETE(URIConfiguration.uriAuthInfo)
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("로그아웃")
  public void signOut() throws Exception {
    setSignedUser();

    apply();

    POST(URIConfiguration.uriAuthSignOut)
        .andDo(print())
        .andDo(docs(ResourceSnippetParameters
            .builder()
            .summary("로그아웃")
            .description(AuthDocs.Data.descriptionAuthorizationAccess()
                + lineSeparator() + lineSeparator()
                + AuthDocs.Data.descriptionRsa()
                + lineSeparator() + lineSeparator()
                + AuthDocs.Data.descriptionRole(ROLE_MANAGER_MAJOR, ROLE_MANAGER_MINOR, ROLE_USER))
            .build()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Access 토큰 갱신")
  public void refreshToken() throws Exception {
    setSignedUser();

    apply();

    POST_TOKEN_BODY(URIConfiguration.uriAuthRefresh,
        threadRefresh.get(),
        RefreshTokenRequest
            .builder()
            .accessToken(threadAccess.get())
            .build())
        .andDo(print())
        .andDo(docs(ResourceSnippetParameters
            .builder()
            .summary("Access 토큰 갱신")
            .description(AuthDocs.Data.descriptionAuthorizationRefresh()
                + lineSeparator() + lineSeparator()
                + AuthDocs.Data.descriptionRsa()
                + lineSeparator() + lineSeparator()
                + AuthDocs.Data.descriptionRole(ROLE_MANAGER_MAJOR, ROLE_MANAGER_MINOR, ROLE_USER))
            .requestFields(AuthDocs.Request.refreshToken())
            .responseFields(AuthDocs.Response.refreshToken())
            .build()))
        .andExpect(status().isOk());
  }

}
