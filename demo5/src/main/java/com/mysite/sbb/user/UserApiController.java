package com.mysite.sbb.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    // 로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 인증 성공 시 SecurityContext에 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션에 SecurityContext 저장
            HttpSession session = request.getSession();
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            // 사용자 이름 가져오기
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            return ResponseEntity.ok(Map.of(
                    "message", "로그인 성공",
                    "username", username
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다."));
        }
    }

    // 현재 사용자 정보 반환
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(Map.of("username", userDetails.getUsername()));
        } else {
            return ResponseEntity.status(401).body("Unauthenticated");
        }
    }
}
