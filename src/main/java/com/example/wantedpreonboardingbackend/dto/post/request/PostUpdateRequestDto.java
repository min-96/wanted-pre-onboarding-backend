package com.example.wantedpreonboardingbackend.dto.post.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostUpdateRequestDto {
    String title;
    String content;
}
