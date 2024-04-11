package run.freshr.common.configurations;

public class URIConfiguration extends URIConfigurationAware {

  public static final String uriAuthCrypto = "/crypto"; // RSA 요청
  public static final String uriAuthSignIn = "/sign-in"; // 로그인
  public static final String uriAuthSignOut = "/sign-out"; // 로그아웃
  public static final String uriAuthPassword = "/password"; // 비밀번호 변경
  public static final String uriAuthInfo = "/info"; // 내 정보
  public static final String uriAuthRefresh = "/refresh"; // Access 토큰 갱신

}
