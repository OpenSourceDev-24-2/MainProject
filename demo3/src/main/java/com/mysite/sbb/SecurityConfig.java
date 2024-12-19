package com.mysite.sbb;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/h2-console/**", "/api/**")
						.disable())
				.authorizeHttpRequests(auth -> auth
						// 정적 리소스 허용
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						// 로그인 엔드포인트 허용
						.requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
						// 모든 OPTIONS 요청 허용 (CORS preflight 요청)
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						// GET /api/questions/** 요청 허용
						.requestMatchers(HttpMethod.GET, "/api/questions/**").permitAll()
						// 인증이 필요한 API 엔드포인트
						.requestMatchers(HttpMethod.POST, "/api/questions/*/answers").authenticated()
						.requestMatchers(HttpMethod.DELETE, "/api/answers/**").authenticated()
						// 기타 공개 리소스
						.requestMatchers("/", "/h2-console/**", "/css/**", "/js/**", "/img/**",
								"/error", "/api/questions/create", "/images/**", "/favicon.ico", "/index.html",
								"/board/**", "/style.css").permitAll()
						.anyRequest().authenticated())
				// formLogin 제거
				.headers(headers -> headers.addHeaderWriter(
						new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.logout(logout -> logout
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

		// 명시적인 Origin 허용
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
