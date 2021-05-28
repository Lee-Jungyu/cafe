package com.jglee.cafe.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupDto {
    private String email;
    private String password;
    private String roles;
}
