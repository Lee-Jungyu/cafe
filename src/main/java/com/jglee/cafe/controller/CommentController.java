package com.jglee.cafe.controller;

import com.jglee.cafe.dto.CommentDto;
import com.jglee.cafe.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity createComment(@RequestBody CommentDto dto) {

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
    public ResponseEntity updateComment(@PathVariable Long id) {
        return new ResponseEntity("success", HttpStatus.OK);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id) {
        return new ResponseEntity("success", HttpStatus.OK);
    }
}
