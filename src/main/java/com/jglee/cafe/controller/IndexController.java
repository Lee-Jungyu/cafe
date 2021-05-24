package com.jglee.cafe.controller;

import com.jglee.cafe.config.JwtTokenProvider;
import com.jglee.cafe.domain.*;
import com.jglee.cafe.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {

        // 현재 선택된 게시글 카테고리가 무엇인지 확인
        if(request.getParameter("category") != null) {
            Long categoryId = Long.parseLong(request.getParameter("category"));
            Category category = categoryRepository.findById(categoryId).orElse(null);

            if(category != null) {
                model.addAttribute("category", category.getName());
            }
            else {
                model.addAttribute("category", "전체글보기");
            }
        }
        else {
            model.addAttribute("category", "전체글보기");
        }

        String token = jwtTokenProvider.resolveToken(request);
        if(token == null) return "index";

        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null) {
            model.addAttribute("email", user.getEmail());
            model.addAttribute("role", user.getRoles().get(0));
        }

        return "index";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @GetMapping("/profile")
    public String profile(Model model, @RequestParam(value = "email") String email) {

        User user = userRepository.findByEmail(email).orElse(null);

        if(user != null) {
            model.addAttribute("email", user.getEmail());
            model.addAttribute("role", user.getRoles().get(0));
        }
        else {
            model.addAttribute("email", "not-found");
            model.addAttribute("role", "not-found");
        }

        return "profile";
    }

    @GetMapping("/manage-category")
    public String manageCategory(Model model) {
        return "manage-category";
    }

    @GetMapping("/new-post")
    public String newPost(Model model) {
        return "new-post";
    }

    @GetMapping("/read-post")
    public String readPost(Model model, HttpServletRequest request) {
        Long postId = Long.parseLong(request.getParameter("postId"));

        List<CommentDto> comments = commentRepository.findAllByPost_Id(postId)
                .stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());

        model.addAttribute("comments", comments);

        return "read-post";
    }
}
