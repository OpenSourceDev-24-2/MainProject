package com.mysite.sbb.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {
    @NotEmpty(message = "아이디는 필수 입력 항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;
}
