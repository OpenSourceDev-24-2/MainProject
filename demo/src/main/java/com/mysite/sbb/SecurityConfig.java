package com.mysite.sbb;

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

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors().and() // CORS 설정
				.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
						.requestMatchers("/h2-console/**", "/api/user/**", "/css/**", "/js/**").permitAll() // REST API와 정적 리소스 허용
						.anyRequest().authenticated() // 나머지 요청은 인증 필요
				)
				.csrf((csrf) -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/**")) // REST API와 H2 콘솔의 CSRF 비활성화
				.headers((headers) -> headers.addHeaderWriter(
						new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))) // H2 콘솔 허용
				.formLogin((formLogin) -> formLogin
						.loginPage("/page-login.html") // 사용자 정의 로그인 페이지
						.loginProcessingUrl("/process-login") // 로그인 처리 경로
						.defaultSuccessUrl("/") // 로그인 성공 시 이동할 경로
						.failureUrl("/page-login.html?error=true") // 로그인 실패 시 이동할 경로
						.permitAll()
				)
				.logout((logout) -> logout
						.logoutUrl("/logout") // 로그아웃 처리 URL
						.logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 URL
						.invalidateHttpSession(true) // 세션 무효화
						.deleteCookies("JSESSIONID") // 쿠키 삭제
				);

		return http.build();
	}



	// CORS 설정
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:5501", "http://127.0.0.1:5501")); // 프론트엔드 주소
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
		configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
		configuration.setAllowCredentials(true); // 쿠키 허용

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
