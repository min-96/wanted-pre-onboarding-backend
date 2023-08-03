package com.example.wantedpreonboardingbackend.dto.post.response;

import com.example.wantedpreonboardingbackend.domain.entity.Post;
import com.example.wantedpreonboardingbackend.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostDetailResponseDto {

    String title;
    String content;
    String writerEmail;

    public PostDetailResponseDto(Post post){
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writerEmail = post.getUser().getEmail();
    }

}
