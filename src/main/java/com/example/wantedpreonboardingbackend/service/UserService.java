package com.example.wantedpreonboardingbackend.service;


import com.example.wantedpreonboardingbackend.domain.entity.User;
import com.example.wantedpreonboardingbackend.domain.repository.UserRepository;
import com.example.wantedpreonboardingbackend.dto.user.request.UserSignInRequestDto;
import com.example.wantedpreonboardingbackend.dto.user.request.UserSignUpRequestDto;
import com.example.wantedpreonboardingbackend.util.global.GlobalErrorCode;
import com.example.wantedpreonboardingbackend.util.global.GlobalException;
import com.example.wantedpreonboardingbackend.util.passwordEncoder.BcryptEncoder;
import com.example.wantedpreonboardingbackend.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BcryptEncoder bcryptEncoder;
    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;


    public void signUp(UserSignUpRequestDto requestDto) {

        if(userRepository.existsByEmail(requestDto.getEmail())) {
            throw new GlobalException(GlobalErrorCode.EMAIL_DUPLICATION);
        }

        String encPassword = bcryptEncoder.encryptPassword(requestDto.getPassword());

        userRepository.save(requestDto.toEntity(encPassword, requestDto.getEmail()));
    }

    public String signIn(UserSignInRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.EMAIL_NOT_MATCH));

        if (!bcryptEncoder.checkPassword(requestDto.getPassword(), user.getPassword())) {
            throw new GlobalException(GlobalErrorCode.PASSWORD_NOT_MATCH);
        }

        return jwtTokenProvider.createAccessToken(user.getId());
    }


}
