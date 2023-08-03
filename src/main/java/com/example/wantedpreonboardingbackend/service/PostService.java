package com.example.wantedpreonboardingbackend.service;

import com.example.wantedpreonboardingbackend.domain.entity.User;
import com.example.wantedpreonboardingbackend.domain.repository.PostRepository;
import com.example.wantedpreonboardingbackend.domain.repository.UserRepository;
import com.example.wantedpreonboardingbackend.dto.post.request.PostCreateRequestDto;
import com.example.wantedpreonboardingbackend.dto.post.request.PostUpdateRequestDto;
import com.example.wantedpreonboardingbackend.dto.post.response.PostDetailResponseDto;
import com.example.wantedpreonboardingbackend.dto.post.response.PostListResponseDto;
import com.example.wantedpreonboardingbackend.util.global.GlobalErrorCode;
import com.example.wantedpreonboardingbackend.util.global.GlobalException;
import com.example.wantedpreonboardingbackend.util.jwt.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createPost(Long userId, PostCreateRequestDto requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));

        postRepository.save(requestDto.toEntity(user));
    }


    @Transactional(readOnly = true)
    public PostDetailResponseDto getPost(Long id) {
        return postRepository.findPostById(id).map(PostDetailResponseDto::new)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<PostListResponseDto> getPostList(Pageable pageable) {
        return postRepository.findAllByOrderByIdAsc(pageable).map(p -> new PostListResponseDto(p.getId(), p.getTitle()));
}

    @Transactional
    public void updatePost(Long id, PostUpdateRequestDto requestDto) {
        postRepository.findUserPost(id, UserContext.userId.get())
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.UNAUTHORIZED_USER))
                .updatePost(requestDto);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.findUserPost(id, UserContext.userId.get())
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.UNAUTHORIZED_USER));

        postRepository.deleteById(id);
    }
}


