package com.jglee.cafe.service;

import com.jglee.cafe.domain.Post;
import com.jglee.cafe.domain.PostRepository;
import com.jglee.cafe.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long save(PostDto dto) {

        return postRepository.save(Post.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .content(dto.getContent())
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

        post.update(dto);

        return postRepository.save(post).getId();
    }

    @Transactional
    public Long delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        postRepository.delete(post);

        return id;
    }
}
