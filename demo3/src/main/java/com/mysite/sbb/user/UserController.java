package com.mysite.sbb.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("유효성 검증에 실패했습니다.");
		}

		if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
		}

		try {
			userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
			return ResponseEntity.ok(Map.of("status", "success", "message", "회원가입이 완료되었습니다."));
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status", "error", "message", "이미 등록된 사용자입니다."));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "회원가입 실패: " + e.getMessage()));
		}
	}
}

