package com.jglee.cafe.controller;

import com.jglee.cafe.config.JwtTokenProvider;
import com.jglee.cafe.domain.User;
import com.jglee.cafe.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private HttpSession httpSession;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

        String token = jwtTokenProvider.resolveToken(request);
        if(token == null) return "index";

        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null) model.addAttribute("email", user.getEmail());

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
}
