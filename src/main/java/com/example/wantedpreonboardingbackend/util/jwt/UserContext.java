package com.example.wantedpreonboardingbackend.util.jwt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserContext {
    public static ThreadLocal<Long> userId = new ThreadLocal<>();

    public static void remove() {
        if (userId != null)
            userId.remove();
    }

    public static void setUserId(Long userId) {
        UserContext.userId.set(userId);
        log.info("userId: {}", userId);
    }
}
