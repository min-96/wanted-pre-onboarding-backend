package com.example.wantedpreonboardingbackend.dto.post.request;


import com.example.wantedpreonboardingbackend.domain.entity.Post;
import com.example.wantedpreonboardingbackend.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostCreateRequestDto {

    @NonNull
    private String title;
    @NonNull
    private String content;

    public Post toEntity(User user){
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();

    }

}
