package com.example.wantedpreonboardingbackend.user;


import com.example.wantedpreonboardingbackend.domain.entity.User;
import com.example.wantedpreonboardingbackend.domain.repository.UserRepository;
import com.example.wantedpreonboardingbackend.dto.user.request.UserSignInRequestDto;
import com.example.wantedpreonboardingbackend.dto.user.request.UserSignUpRequestDto;
import com.example.wantedpreonboardingbackend.service.UserService;
import com.example.wantedpreonboardingbackend.util.global.GlobalErrorCode;
import com.example.wantedpreonboardingbackend.util.global.GlobalException;
import com.example.wantedpreonboardingbackend.util.jwt.JwtTokenProvider;
import com.example.wantedpreonboardingbackend.util.passwordEncoder.BcryptEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepository;
    @Mock
    BcryptEncoder bcryptEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    UserService userService;

    private String email;
    private String password;
    private User user;
    private UserSignUpRequestDto signupRequestDto;

    private UserSignInRequestDto signInRequestDto;

    @BeforeEach
    void setUp() {
        email = "minyong@wanted.com";
        password = "@Dkssudgktpdy";

        user = User.builder()
                .email(email)
                .password(password)
                .build();

        signupRequestDto = new UserSignUpRequestDto();
        signupRequestDto.setEmail(email);
        signupRequestDto.setPassword(password);

        signInRequestDto = new UserSignInRequestDto();
        signInRequestDto.setEmail(email);
        signInRequestDto.setPassword(password);


    }

    @Test
    void signUpTest() {

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(bcryptEncoder.encryptPassword(user.getPassword())).thenReturn("encryptedPassword");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.signUp(signupRequestDto);

        verify(userRepository).existsByEmail(email);
        verify(bcryptEncoder).encryptPassword(password);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals(email, savedUser.getEmail());
        assertEquals("encryptedPassword", savedUser.getPassword());

    }


    @Test
    void signInTest() {

        when(userRepository.findByEmail(signInRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(bcryptEncoder.checkPassword(signInRequestDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(user.getId())).thenReturn("accessToekn");

        String token = userService.signIn(signInRequestDto);

        verify(userRepository).findByEmail(signInRequestDto.getEmail());
        verify(bcryptEncoder).checkPassword(signInRequestDto.getPassword(), user.getPassword());
        verify(jwtTokenProvider).createAccessToken(user.getId());

        assertEquals("accessToekn", token);
    }

    @Test
    void signUpDuplicateEmailTest() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        GlobalException exception = assertThrows(GlobalException.class, () -> userService.signUp(signupRequestDto));
        assertEquals(exception.getErrorCode().getMessage(), GlobalErrorCode.EMAIL_DUPLICATION.getMessage());
    }


    @Test
    void signInTestEmailNotMatch() {

        when(userRepository.findByEmail(signInRequestDto.getEmail())).thenReturn(Optional.empty());

        GlobalException exception = assertThrows(GlobalException.class, () -> userService.signIn(signInRequestDto));
        assertEquals(exception.getErrorCode().getMessage(), GlobalErrorCode.EMAIL_NOT_MATCH.getMessage());
    }

    @Test
    void signInTestPasswordNotMatch() {
        when(userRepository.findByEmail(signInRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(bcryptEncoder.checkPassword(anyString(), anyString())).thenReturn(false);


        GlobalException exception = assertThrows(GlobalException.class, () -> userService.signIn(signInRequestDto));
        assertEquals(exception.getErrorCode().getMessage(), GlobalErrorCode.PASSWORD_NOT_MATCH.getMessage());

    }

}
