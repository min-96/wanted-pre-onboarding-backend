package com.example.wantedpreonboardingbackend.controller;


import com.example.wantedpreonboardingbackend.dto.user.request.UserSignInRequestDto;
import com.example.wantedpreonboardingbackend.dto.user.request.UserSignUpRequestDto;
import com.example.wantedpreonboardingbackend.service.UserService;
import com.example.wantedpreonboardingbackend.util.global.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        userService.signUp(requestDto);
        return ResponseMessage.SuccessResponse("회원가입이 성공적으로 완료되었습니다.",null);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody UserSignInRequestDto requestDto) {
        String accessToken = userService.signIn(requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        ResponseMessage<?> responseMessage = ResponseMessage.SuccessResponse("로그인 성공", null).getBody();

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(responseMessage);
    }

}
