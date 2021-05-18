package com.jglee.cafe.controller;

import com.jglee.cafe.domain.Post;
import com.jglee.cafe.domain.PostRepository;
import com.jglee.cafe.dto.PostDto;
import com.jglee.cafe.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity createPost(@RequestBody PostDto dto) {
        if(dto.getTitle().length() == 0)
            return new ResponseEntity("You must enter title", HttpStatus.BAD_REQUEST);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails author = (UserDetails) authentication.getPrincipal();

        String email = author.getUsername();
        dto.setAuthor(email);

        System.out.println(email);

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

    @GetMapping("/post/user/{email}")
    public List<PostDto> findAllByUser_Email(@PathVariable String email){
        return postService.findAllByAuthor_Email(email);
    }

    @GetMapping("/post/category/{id}")
    public List<PostDto> findAllByCategory_Id(@PathVariable Long id) {
        return postService.findAllByCategory_Id(id);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity updatePost(@PathVariable Long id, @RequestBody PostDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();

        String email = user.getUsername();
        String role = user.getAuthorities().toArray()[0].toString();

        Post post = postRepository.findById(id).orElse(null);

        if(post == null)
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        if(!role.equals("ROLE_ADMIN") && !post.getAuthor().getEmail().equals(email))
            return new ResponseEntity("You are not author or admin", HttpStatus.FORBIDDEN);

        if(dto.getTitle().length() == 0)
            return new ResponseEntity("You must enter post title", HttpStatus.BAD_REQUEST);

        postService.update(id, dto);

        return new ResponseEntity("success", HttpStatus.OK);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity deletePost(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();

        String email = user.getUsername();
        String role = user.getAuthorities().toArray()[0].toString();

        Post post = postRepository.findById(id).orElse(null);

        if(post == null)
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        if(!role.equals("ROLE_ADMIN") && !post.getAuthor().getEmail().equals(email))
            return new ResponseEntity("You are not author or admin", HttpStatus.FORBIDDEN);

        postService.delete(id);

        return new ResponseEntity("success", HttpStatus.OK);
    }

}
