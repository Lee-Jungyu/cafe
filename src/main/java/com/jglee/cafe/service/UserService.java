package com.jglee.cafe.service;

import com.jglee.cafe.domain.User;
import com.jglee.cafe.domain.UserRepository;
import com.jglee.cafe.dto.UserSignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    // (param: email, return: UserDetails, throws: UsernameNotFoundException)
    // 기본적인 반환타입은 UserDetail: User(UserDetails를 상속받음) -> 자동으로 다운 캐스팅
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현

        return userRepository.findByEmail(email).orElse(null);
    }

    // 회원정보 저장
    // (param: UserDto(회원정보가 들어있는 dto), return: id(pk))
    public Long save(UserSignupDto dto) {
        String role = dto.getRoles();
        List<String> roles = Arrays.asList(role.split(",").clone());

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .roles(roles)
                .password(dto.getPassword())
                .build()).getId();
    }
}
