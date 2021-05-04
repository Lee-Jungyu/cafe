package com.jglee.cafe.domain;

import com.jglee.cafe.dto.CategoryDto;
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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Builder
    public Category(String name) {
        this.name = name;
    }

    public void update(CategoryDto dto) {
        this.name = dto.getName();
    }
}

