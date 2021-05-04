package com.jglee.cafe.service;

import com.jglee.cafe.domain.Category;
import com.jglee.cafe.domain.CategoryRepository;
import com.jglee.cafe.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService{

    private final CategoryRepository categoryRepository;

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

        categoryRepository.delete(category);
        return id;
    }
}
