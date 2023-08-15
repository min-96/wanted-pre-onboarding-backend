package com.example.wantedpreonboardingbackend.controller;


import com.example.wantedpreonboardingbackend.dto.post.request.PostCreateRequestDto;
import com.example.wantedpreonboardingbackend.dto.post.request.PostUpdateRequestDto;
import com.example.wantedpreonboardingbackend.dto.post.response.PostListResponseDto;
import com.example.wantedpreonboardingbackend.service.PostService;
import com.example.wantedpreonboardingbackend.util.global.ResponseMessage;
import com.example.wantedpreonboardingbackend.util.jwt.UserContext;
import com.example.wantedpreonboardingbackend.util.customAnnotation.ValidToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @ValidToken
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostCreateRequestDto requestDto) {
        log.info("userId: {}", UserContext.userId.get());
        postService.createPost(UserContext.userId.get(),requestDto);
        return ResponseMessage.SuccessResponse("게시물이 성공적으로 등록되었습니다.",null);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        return ResponseMessage.SuccessResponse("게시글이 성공적으로 조회되었습니다.",postService.getPost(id));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPostList(Pageable pageable) {
        return ResponseMessage.SuccessResponse("전체 게시글들이 성공적으로 조회되었습니다.",postService.getPostList(pageable));
    }


    @ValidToken
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id,@RequestBody PostUpdateRequestDto requestDto) {
        postService.updatePost(id, requestDto);
        return ResponseMessage.SuccessResponse("게시글이 성공적으로 수정되었습니다.",null);
    }

    @ValidToken
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseMessage.SuccessResponse("게시글이 성공적으로 삭제되었습니다.",null);
    }

}
