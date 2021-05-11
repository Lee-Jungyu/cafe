package com.jglee.cafe.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthor(String author);
    List<Post> findAllByAuthor_Email(@Param(value = "email") String email);
    List<Post> findAllByCategory_Id(@Param(value = "id") Long id);
}
