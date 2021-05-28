package com.jglee.cafe.post.dto;

import com.jglee.cafe.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private Long categoryId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostDto (Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getAuthor().getEmail();
        this.content = post.getContent();
        this.categoryId = post.getCategory().getId();
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();
    }
}
