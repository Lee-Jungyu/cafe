package com.jglee.cafe.auth;

import com.jglee.cafe.user.domain.User;
import com.jglee.cafe.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private String secretKey = "secretKey";
    private long tokenValidTime = 30 * 60 * 1000L;
    private final UserService userService;

    // 객체 초기화 secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 토큰 생성
    public String createToken(User member) {
        Claims claims = Jwts.claims().setSubject(member.getId().toString()); // JWT payload에 저장되는 정보 단위
        claims.put("email", member.getEmail());
        claims.put("roles", member.getRoles());
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) //set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) //사용할 알고리즘, signature에 들어갈 secret 값
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByEmail(this.getEmail(token));
        if(userDetails == null) return null;
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getEmail(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email");
    }

    // Request의 Header에서 token 값을 가져 옴
    public String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("jwt")) {
                    Cookie jwtCookie = cookies[i];
                    String token = jwtCookie.getValue();

                    if (!validateToken(token)) return null;

                    return token;
                }
            }
        }

        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

