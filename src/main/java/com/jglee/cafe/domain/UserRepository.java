package com.jglee.cafe.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // findBy Entity 필드명을 입력하면 해당 필드를 가진 검색 쿼리를 실행한 결과 전달
    // 자세한건 JpaRepository findBy 메소드 검색
    Optional<User> findByEmail(String email);
}
