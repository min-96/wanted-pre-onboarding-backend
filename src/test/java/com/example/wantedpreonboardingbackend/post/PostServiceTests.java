package com.example.wantedpreonboardingbackend.post;


import com.example.wantedpreonboardingbackend.domain.entity.Post;
import com.example.wantedpreonboardingbackend.domain.entity.User;
import com.example.wantedpreonboardingbackend.domain.repository.PostRepository;
import com.example.wantedpreonboardingbackend.domain.repository.UserRepository;
import com.example.wantedpreonboardingbackend.dto.post.request.PostCreateRequestDto;
import com.example.wantedpreonboardingbackend.dto.post.request.PostUpdateRequestDto;
import com.example.wantedpreonboardingbackend.dto.post.response.PostDetailResponseDto;
import com.example.wantedpreonboardingbackend.dto.post.response.PostListResponseDto;
import com.example.wantedpreonboardingbackend.service.PostService;
import com.example.wantedpreonboardingbackend.util.jwt.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {
    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PostService postService;

    private String title;
    private String content;
    private Post post;
    private User user;

    PostCreateRequestDto postCreateRequestDto;

    PostUpdateRequestDto postUpdateRequestDto;


    @BeforeEach
    void setUp() {
        title = "title";
        content = "content";

        user = User.builder()
                .email("asdasd@wated.com")
                .password("!Password11")
                .build();

        post = Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();


        postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setTitle(title);
        postCreateRequestDto.setContent(content);

        postUpdateRequestDto = new PostUpdateRequestDto();
        postUpdateRequestDto.setTitle(title+"update");
        postUpdateRequestDto.setContent(content+"update");

    }


    @Test
    void createPostTest() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        postService.createPost(1L, postCreateRequestDto);

        verify(postRepository).save(postCaptor.capture());
        Post capturedPost = postCaptor.getValue();

        assertEquals(title, capturedPost.getTitle());
        assertEquals(content, capturedPost.getContent());
    }

    @Test
    void getPostTest() {

        when(postRepository.findPostById(1L)).thenReturn(Optional.of(post));

        PostDetailResponseDto postDto = postService.getPost(1L);

        verify(postRepository).findPostById(1L);

        assertEquals(title, postDto.getTitle());
        assertEquals(content, postDto.getContent());
        assertEquals(user.getEmail(), postDto.getWriterEmail());
    }


    @Test
    void getPostListTest() {
        Pageable pageable = PageRequest.of(0, 3);

        List<Post> posts = Arrays.asList(
                Post.builder().title("title1").content("content1").user(user).build(),
                Post.builder().title("title2").content("content2").user(user).build(),
                Post.builder().title("title3").content("content3").user(user).build()
        );
        Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());

        when(postRepository.findAllByOrderByIdAsc(pageable)).thenReturn(postPage);

        Page<PostListResponseDto> result = postService.getPostList(pageable);

        assertEquals(posts.size(), result.getContent().size());
        assertEquals(posts.get(0).getTitle(), result.getContent().get(0).getTitle());
        assertEquals(posts.get(1).getTitle(), result.getContent().get(1).getTitle());
        assertEquals(posts.get(2).getTitle(), result.getContent().get(2).getTitle());
    }


    @Test
    void updatePostTest() {
        Long postId = 1L;
        Long userId = 1L;

        String title = "Title";
        String content = "Content";

        Post post = Mockito.mock(Post.class);

        when(post.getTitle()).thenReturn(title + "update");
        when(post.getContent()).thenReturn(content + "update");
        when(postRepository.findUserPost(userId, postId)).thenReturn(Optional.of(post));

        UserContext.setUserId(userId);

        postService.updatePost(postId, postUpdateRequestDto);

        verify(postRepository).findUserPost(userId, postId);
        verify(post).updatePost(any(PostUpdateRequestDto.class));

        assertEquals(title + "update", post.getTitle());
        assertEquals(content + "update", post.getContent());

        UserContext.remove();
    }

    @Test
    void deletePostTest() {
    Long userId = 1L;
    Long postId = 1L;

    Post post = mock(Post.class);

    when(postRepository.findUserPost(postId, userId)).thenReturn(Optional.of(post));

    UserContext.setUserId(userId);
    postService.deletePost(postId);

    verify(postRepository).findUserPost(postId, userId);
    verify(postRepository).deleteById(postId);
    UserContext.remove();
}



}
