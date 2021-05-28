package com.jglee.cafe.post.domain;

import com.jglee.cafe.category.domain.Category;
import com.jglee.cafe.app.domain.BaseTimeEntity;
import com.jglee.cafe.post.dto.PostDto;
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
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_email")
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Post(String title, String content, User author, Category category) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.category = category;
    }

    public void update(PostDto dto, Category category) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.category = category;
    }
}

