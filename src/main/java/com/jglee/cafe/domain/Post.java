package com.jglee.cafe.domain;

import com.jglee.cafe.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity{

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

    public void update(PostDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }
}

