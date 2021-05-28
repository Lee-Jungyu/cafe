package com.jglee.cafe.comment.service;

import com.jglee.cafe.comment.domain.Comment;
import com.jglee.cafe.comment.domain.CommentRepository;
import com.jglee.cafe.comment.dto.CommentDto;
import com.jglee.cafe.post.domain.PostRepository;
import com.jglee.cafe.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

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

    public Long save(CommentDto dto) {
        return commentRepository.save(Comment.builder()
                .author(userRepository.findByEmail(dto.getAuthor())
                        .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다.")))
                .post(postRepository.findById(dto.getPostId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다.")))
                .content(dto.getContent()).build()).getId();
    }

    public Long update(Long id, CommentDto dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 코멘트가 없습니다."));

        comment.update(dto);

        return commentRepository.save(comment).getId();
    }

    public Long delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 코멘트가 없습니다."));

        commentRepository.delete(comment);

        return id;
    }
}
