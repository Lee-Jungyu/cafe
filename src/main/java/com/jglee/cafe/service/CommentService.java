package com.jglee.cafe.service;

import com.jglee.cafe.domain.Comment;
import com.jglee.cafe.domain.CommentRepository;
import com.jglee.cafe.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private CommentRepository commentRepository;

    @Transactional
    public CommentDto findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 코멘트가 없습니다."));

        return new CommentDto(comment);
    }

    @Transactional
    public List<CommentDto> findAll() {
        return commentRepository.findAll()
                .stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }
}
