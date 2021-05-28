package com.jglee.cafe.category.controller;

import com.jglee.cafe.auth.JwtTokenProvider;
import com.jglee.cafe.category.dto.CategoryDto;
import com.jglee.cafe.category.service.CategoryService;
import com.jglee.cafe.category.domain.CategoryRepository;
import com.jglee.cafe.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final UserRepository userRepository;

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/category")
    @ResponseBody
    public ResponseEntity createCategory(@RequestBody CategoryDto dto) {
        if(dto.getName().length() == 0)
            return new ResponseEntity("You must enter category name", HttpStatus.BAD_REQUEST);

        if(categoryService.findByName(dto.getName()) != null)
            return new ResponseEntity("Same category name is not allowed", HttpStatus.CONFLICT);

        categoryService.save(dto);

        return new ResponseEntity("success", HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public CategoryDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @GetMapping("/category")
    public List<CategoryDto> findAll() {
        return categoryService.findAll();
    }

    @PutMapping("/category/{id}")
    public ResponseEntity updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
        if(dto.getName().length() == 0)
            return new ResponseEntity("You must enter category name", HttpStatus.BAD_REQUEST);

        if(categoryService.findById(id) == null)
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        if(categoryService.findByName(dto.getName()) != null)
            return new ResponseEntity("Same category name is not allowed", HttpStatus.CONFLICT);

        categoryService.update(id, dto);

        return new ResponseEntity("success", HttpStatus.OK);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        if(categoryService.findById(id) == null)
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        categoryService.delete(id);

        return new ResponseEntity("success", HttpStatus.OK);
    }

}
