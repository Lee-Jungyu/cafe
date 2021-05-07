package com.jglee.cafe.controller;

import com.jglee.cafe.config.JwtTokenProvider;
import com.jglee.cafe.domain.PostRepository;
import com.jglee.cafe.domain.UserRepository;
import com.jglee.cafe.dto.PostDto;
import com.jglee.cafe.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final UserRepository userRepository;

    private final PostService postService;
    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity createCategory(@RequestBody PostDto dto) {
        if(dto.getTitle().length() == 0)
            return new ResponseEntity("You must enter title", HttpStatus.BAD_REQUEST);

        postService.save(dto);

        return new ResponseEntity("success", HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public PostDto findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/post")
    public List<PostDto> findAll() {
        return postService.findAll();
    }

    @PutMapping("/post/{id}")
    public ResponseEntity updateCategory(@PathVariable Long id, @RequestBody PostDto dto) {
        if(postService.findById(id) == null)
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        if(dto.getTitle().length() == 0)
            return new ResponseEntity("You must enter post title", HttpStatus.BAD_REQUEST);

        postService.update(id, dto);

        return new ResponseEntity("success", HttpStatus.OK);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        if(postService.findById(id) == null)
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        postService.delete(id);

        return new ResponseEntity("success", HttpStatus.OK);
    }

}
