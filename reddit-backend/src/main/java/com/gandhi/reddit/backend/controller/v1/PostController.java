package com.gandhi.reddit.backend.controller.v1;

import com.gandhi.reddit.backend.dto.PostDto;
import com.gandhi.reddit.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable("id") final long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPost());
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postDto));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostDto>> getAllPostsByUsername(@PathVariable("username") final String username) {
        return ResponseEntity.ok(postService.getPostsByUsername(username));
    }

    @GetMapping("/subreddit/{id}")
    public ResponseEntity<List<PostDto>> getAllPostsByUsername(@PathVariable("id") final long subredditId) {
        return ResponseEntity.ok(postService.getPostsBySubreddit(subredditId));
    }

//
//    @PatchMapping("/{id}")
//    public ResponseEntity<PostDto> patchPost(@RequestBody PostDto postDto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postDto));
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<PostDto> patchPost(@RequestBody PostDto postDto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postDto));
//    }
}
