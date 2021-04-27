package com.jglee.cafe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       // 헤더에서 JWT를 받아옴
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
       // 토큰 유효성 검사
        if(token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옴
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext에 Authentication 객체 저장
            if(authentication != null) SecurityContextHolder.getContext().setAuthentication(authentication);
            else {
                HttpServletResponse hResponse = (HttpServletResponse) response;

                Cookie jwtCookie = new Cookie("jwt", null);
                jwtCookie.setMaxAge(0); // 쿠키의 유효기간 0 -> 쿠키 삭제
                jwtCookie.setPath("/"); // 모든 경로에서 접근 가능한 쿠키

                hResponse.addCookie(jwtCookie);
            }
        }
        chain.doFilter(request, response);
    }
}
