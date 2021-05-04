package com.jglee.cafe.dto;

import com.jglee.cafe.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private List<String> roles;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
