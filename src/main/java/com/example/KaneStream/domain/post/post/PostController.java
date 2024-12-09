package com.example.KaneStream.domain.post.post;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> createPost(PostRequest request) {
        PostDto post=postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    @DeleteMapping("/delete-{id}")
    public void deletePost(@PathVariable(name = "id") UUID postId) {
        postService.deletePost(postId);
    }

    @PatchMapping(value = "/update-post-{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> updatePost(@PathVariable(name = "id") UUID postId,PostRequest request) {
        PostDto postDto=postService.updatePost(postId,request);
        return ResponseEntity.status(HttpStatus.OK).body(postDto);
    }

    @GetMapping("/get-list-post")
    public ResponseEntity<Page<PostDto>> getPostList(GetPostRequest request) {
        Page<PostDto> posts=postService.getPosts(request);
        return ResponseEntity.status(HttpStatus.OK).body(posts);

    }

    @GetMapping("/get-total-page")
    public ResponseEntity<Map<String, Object>> getPostTotalPage(GetPostRequest request) {
        Page<PostDto> posts=postService.getPosts(request);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", posts.getNumber());
        response.put("totalPages", posts.getTotalPages());
        response.put("totalElements", posts.getTotalElements());
        response.put("size", posts.getSize());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
