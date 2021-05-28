package com.jglee.cafe.comment.domain;

import com.jglee.cafe.app.domain.BaseTimeEntity;
import com.jglee.cafe.comment.dto.CommentDto;
import com.jglee.cafe.post.domain.Post;
import com.jglee.cafe.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_email")
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(String content, User author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
    }

    public void update(CommentDto dto) {
        this.content = dto.getContent();
    }
}
