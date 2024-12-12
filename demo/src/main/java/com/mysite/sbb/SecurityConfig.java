package com.mysite.sbb;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeRequests((requests) -> requests
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/api/answers/**").authenticated() // DELETE 요청 인증 필요
						.requestMatchers("/h2-console/**", "/api/user/**", "/css/**", "/js/**", "/api/**", "/error", "/api/questions/create").permitAll()
						.anyRequest().authenticated())
				.csrf((csrf) -> csrf
						.ignoringRequestMatchers("/h2-console/**", "/api/**")
						.disable())
				.headers((headers) -> headers.addHeaderWriter(
						new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
				.formLogin((formLogin) -> formLogin
						.loginPage("/page-login.html")
						.loginProcessingUrl("/process-login")
						.defaultSuccessUrl("/")
						.failureHandler((request, response, exception) -> {
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 반환
							response.setContentType("application/json");
							response.getWriter().write("로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
						})						.permitAll())
				.logout((logout) -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll());
		return http.build();
	}

	@Bean
	public HttpFirewall httpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowSemicolon(true); // ";" 허용
		return firewall;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// 명시적인 Origin 허용 (setAllowedOrigins와 setAllowedOriginPatterns는 함께 사용하지 않습니다)
		configuration.setAllowedOriginPatterns(List.of("http://127.0.0.1:*", "http://localhost:8080"));

		// 허용할 HTTP 메서드 추가
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		// 허용할 헤더
		configuration.setAllowedHeaders(List.of("Content-Type", "Authorization"));

		// 자격 증명 허용
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}

