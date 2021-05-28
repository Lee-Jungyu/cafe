package com.jglee.cafe.category.service;

import com.jglee.cafe.category.domain.Category;
import com.jglee.cafe.category.domain.CategoryRepository;
import com.jglee.cafe.category.dto.CategoryDto;
import com.jglee.cafe.post.domain.Post;
import com.jglee.cafe.post.domain.PostRepository;
import com.jglee.cafe.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService{

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long save(CategoryDto dto) {

        return categoryRepository.save(Category.builder()
                .name(dto.getName())
                .build()).getId();
    }

    @Transactional
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));

        return new CategoryDto(category);
    }

    @Transactional
    public CategoryDto findByName(String name) {
        Category category = categoryRepository.findByName(name).orElse(null);

        if(category == null) return null;

        return new CategoryDto(category);
    }

    @Transactional
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));

        category.update(dto);

        return categoryRepository.save(category).getId();
    }

    @Transactional
    public Long delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));

        // 카테고리 삭제 시 카테고리에 있는 게시물들을 자유게시판으로 옮김
        List<Post> postsInCategory = postRepository.findAllByCategory_Id(id);
        Category freeboard = categoryRepository.findById(1L).orElse(null);
        for(Post post : postsInCategory) {
            post.update(new PostDto(post), freeboard);
        }

        categoryRepository.delete(category);
        return id;
    }
}
