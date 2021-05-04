package com.jglee.cafe.controller;

import com.jglee.cafe.config.JwtTokenProvider;
import com.jglee.cafe.domain.User;
import com.jglee.cafe.domain.UserRepository;
import com.jglee.cafe.dto.UserDto;
import com.jglee.cafe.dto.UserLoginDto;
import com.jglee.cafe.dto.UserSignupDto;
import com.jglee.cafe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity signup(@RequestBody UserSignupDto dto) {
        // Email을 입력하지 않은 경우
        if(dto.getEmail().length() == 0)
            return new ResponseEntity("You must enter email", HttpStatus.BAD_REQUEST);

        // Password를 입력하지 않은 경우
        if(dto.getPassword().length() == 0)
            return new ResponseEntity("You must enter password", HttpStatus.BAD_REQUEST);

        // Email이 이미 존재하는 경우 (status code 409)
        if(!userRepository.findByEmail(dto.getEmail()).isEmpty())
            return new ResponseEntity("already exist", HttpStatus.CONFLICT);

        // 비밀번호 인코딩
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        dto.setPassword(encoder.encode(dto.getPassword()));

        // 회원 가입 (회원 정보 저장)
        userService.save(dto);

        // 회원 가입 완료 (status code 200)
        return new ResponseEntity("success", HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(@RequestBody UserLoginDto dto, HttpServletResponse response) {

        // Email 혹은 Password를 입력하지 않은 경우 (status code 401)
        if(dto.getEmail().length() == 0)
            return new ResponseEntity("Email을 입력해주세요", HttpStatus.UNAUTHORIZED);
        if(dto.getPassword().length() == 0)
            return new ResponseEntity("Password를 입력해주세요", HttpStatus.UNAUTHORIZED);

        // 유저 검색
        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);

        // 입력한 Email을 가진 계정이 없는 경우 (status code 401)
        if(user == null)
            return new ResponseEntity("Email 또는 Password를 확인해주세요", HttpStatus.UNAUTHORIZED);

        // Password가 일치하지 않은 경우 (status coder 401)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(dto.getPassword(), user.getPassword()))
            return new ResponseEntity("Email 또는 Password를 확인해주세요", HttpStatus.UNAUTHORIZED);

        // 토큰 생성
        String token = jwtTokenProvider.createToken(user);

        // 쿠키 생성
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키의 유효기간은 1주일
        jwtCookie.setPath("/"); // 모든 경로에서 접근 가능한 쿠키
        response.addCookie(jwtCookie);

        // 로그인 (status code 200)
        return new ResponseEntity("success", HttpStatus.OK);
    }

    // 로그아웃
    @PostMapping(value = "/log-out")
    @ResponseBody
    public ResponseEntity logout(HttpServletResponse response) {

        // 쿠키 삭제
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setMaxAge(0); // 쿠키의 유효기간 0 -> 쿠키 삭제
        jwtCookie.setPath("/"); // 모든 경로에서 접근 가능한 쿠키
        response.addCookie(jwtCookie);

        // 로그아웃
        return new ResponseEntity("success", HttpStatus.OK);
    }

    // 유저 정보
    @GetMapping(value = "/user/{id}")
    @ResponseBody
    public UserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    // 모든 유저 정보
    @GetMapping(value = "/user")
    @ResponseBody
    public List<UserDto> findAll() {
        return userService.findAll();
    }

}
