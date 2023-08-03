package com.example.wantedpreonboardingbackend.dto.user.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor
@Setter
public class UserSignInRequestDto {

    @Email(regexp=".+@.+\\.[^.]+", message="이메일 형식이 아닙니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\W).{8,}$", message = "비밀번호는 최소 8자 이상이며, 대문자와 특수 문자를 포함해야 합니다.")
    private String password;
}
