package com.example.wantedpreonboardingbackend.dto.post.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostListResponseDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String title;

    public PostListResponseDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
