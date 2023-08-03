package com.example.wantedpreonboardingbackend.util.interceptor;

import com.example.wantedpreonboardingbackend.util.global.GlobalErrorCode;
import com.example.wantedpreonboardingbackend.util.global.GlobalException;
import com.example.wantedpreonboardingbackend.util.jwt.JwtTokenProvider;
import com.example.wantedpreonboardingbackend.util.jwt.UserContext;
import com.example.wantedpreonboardingbackend.util.customAnnotation.ValidToken;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@ComponentScan
public class ValidInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod;

        if (!(handler instanceof HandlerMethod))
            return true;
        handlerMethod = (HandlerMethod) handler;

        ValidToken annotation = handlerMethod.getMethodAnnotation(ValidToken.class);
        if (annotation == null) {
            return true;
        }


        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                jwtTokenProvider.getClaims(token);
                UserContext.setUserId(jwtTokenProvider.getUserId(token));
            } catch (Exception e) {
                log.error("handleCustomException throw CustomException : {}", GlobalErrorCode.INVALID_TOKEN_VALUE);
                throw new GlobalException(GlobalErrorCode.INVALID_TOKEN_VALUE);
            }
            return true;
        }
        throw new GlobalException(GlobalErrorCode.UNAUTHORIZED_USER);
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserContext.remove();
    }
}
