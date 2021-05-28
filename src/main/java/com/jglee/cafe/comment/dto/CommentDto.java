package com.jglee.cafe.comment.dto;

import com.jglee.cafe.comment.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String author;
    private Long postId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getAuthor().getEmail();
        this.postId = comment.getPost().getId();
        this.createdDate = comment.getCreatedDate();
        this.modifiedDate = comment.getModifiedDate();
    }
}
