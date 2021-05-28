package com.jglee.cafe.comment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost_Id(@Param(value = "id") Long id);
    void deleteAllByPost_Id(@Param(value = "id") Long id);
}
