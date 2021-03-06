package com.jglee.cafe.post.service;

import com.jglee.cafe.category.domain.Category;
import com.jglee.cafe.category.domain.CategoryRepository;
import com.jglee.cafe.comment.domain.CommentRepository;
import com.jglee.cafe.post.domain.Post;
import com.jglee.cafe.post.domain.PostRepository;
import com.jglee.cafe.post.dto.PostDto;
import com.jglee.cafe.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long save(PostDto dto) {

        return postRepository.save(Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(userRepository.findByEmail(dto.getAuthor())
                        .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다.")))
                .category(categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다.")))
                .build()).getId();
    }

    @Transactional
    public PostDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        return new PostDto(post);
    }

    @Transactional
    public List<PostDto> findAllByAuthor(String author) {
        return postRepository.findAllByAuthor(author)
                .stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostDto> findAllByAuthor_Email(String email) {
        return postRepository.findAllByAuthor_Email(email)
                .stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostDto> findAllByCategory_Id(Long id) {
        return postRepository.findAllByCategory_Id(id)
                .stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostDto> findAll() {
        return postRepository.findAll()
                .stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, PostDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));

        post.update(dto, category);
        return postRepository.save(post).getId();
    }

    @Transactional
    public Long delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        commentRepository.deleteAllByPost_Id(id);
        postRepository.delete(post);

        return id;
    }
}
