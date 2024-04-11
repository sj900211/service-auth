package run.freshr.common.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import run.freshr.common.configurations.URIConfiguration;
import run.freshr.common.data.ExceptionsData;

/**
 * Security 설정
 *
 * @author FreshR
 * @apiNote Security 설정
 * @since 2024. 4. 2. 오전 10:03:46
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

  private final TokenProvider tokenProvider;
  private final ExceptionsData exceptionsData;

  /**
   * 비밀번호 암호화 방식 설정
   *
   * @return password encoder
   * @apiNote 비밀번호 암호화 방식 설정
   * @author FreshR
   * @since 2024. 4. 2. 오전 10:03:46
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 권한 체크에서 제외할 URI 설정
   *
   * @return web security customizer
   * @apiNote 권한 체크에서 제외할 URI 설정
   * @author FreshR
   * @since 2024. 4. 2. 오전 10:03:46
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(URIConfiguration.uriFavicon)
        .requestMatchers(GET, URIConfiguration.uriCommonHeartbeat)
        .requestMatchers(POST, URIConfiguration.uriAuthRefresh);
  }

  /**
   * Security 설정
   *
   * @param httpSecurity http security
   * @return security filter chain
   * @throws Exception exception
   * @apiNote Security 설정
   * @author FreshR
   * @since 2024. 4. 2. 오전 10:03:46
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(configurer -> configurer.sessionCreationPolicy(STATELESS))
        .authorizeHttpRequests(registry -> registry.anyRequest().permitAll())
        .headers(headers -> headers.xssProtection(xss -> xss.headerValue(ENABLED_MODE_BLOCK))
            .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'")))
        .addFilterBefore(new TokenAuthenticationFilter(tokenProvider, exceptionsData),
            UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

}
