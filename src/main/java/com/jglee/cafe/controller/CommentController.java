package com.jglee.cafe.controller;

import com.jglee.cafe.domain.Comment;
import com.jglee.cafe.dto.CommentDto;
import com.jglee.cafe.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity createComment(@RequestBody CommentDto dto) {
        if(dto.getContent().length() == 0)
            return new ResponseEntity("You must enter comment", HttpStatus.BAD_REQUEST);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails author = (UserDetails) authentication.getPrincipal();

        String email = author.getUsername();
        dto.setAuthor(email);

        commentService.save(dto);

        return new ResponseEntity("success", HttpStatus.OK);
    }

    @GetMapping("/comment/{id}")
    public CommentDto findById(@PathVariable Long id) {
        return commentService.findById(id);
    }

    @GetMapping("/comment")
    public List<CommentDto> findAll() {
        return commentService.findAll();
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity updateComment(@PathVariable Long id, @RequestBody CommentDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails author = (UserDetails) authentication.getPrincipal();

        String email = author.getUsername();

        CommentDto tmpDto = commentService.findById(id);

        if(tmpDto == null)
            return new ResponseEntity("This comment doesn't exist", HttpStatus.BAD_REQUEST);

        if(!tmpDto.getAuthor().equals(email))
            return new ResponseEntity("You are not author", HttpStatus.FORBIDDEN);

        if(dto.getContent().length() == 0)
            return new ResponseEntity("You must enter comment", HttpStatus.BAD_REQUEST);

        commentService.update(id, dto);

        return new ResponseEntity("success", HttpStatus.OK);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();

        String email = user.getUsername();
        String role = user.getAuthorities().toArray()[0].toString();

        CommentDto tmpDto = commentService.findById(id);

        if(tmpDto == null)
            return new ResponseEntity("This comment doesn't exist", HttpStatus.BAD_REQUEST);

        if(!tmpDto.getAuthor().equals(email) && !role.equals("ROLE_ADMIN"))
            return new ResponseEntity("You are not author or admin", HttpStatus.FORBIDDEN);

        commentService.delete(id);

        return new ResponseEntity("success", HttpStatus.OK);
    }
}
