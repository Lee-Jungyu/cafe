package com.jglee.cafe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jglee.cafe.user.domain.UserRepository;
import com.jglee.cafe.user.dto.UserLoginDto;
import com.jglee.cafe.user.dto.UserSignupDto;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Before
    public void reset() {
        userRepository.deleteAll();
    }

    @Test
    public void testA_회원가입_성공() throws Exception {
        // 유저 등록 (성공)
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("a@a.com");
        userSignupDto.setPassword("asdfqwer");
        userSignupDto.setRoles("ROLE_USER");

        String info = objectMapper.writeValueAsString(userSignupDto);

        mockMvc.perform(post("/signup")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testB_회원가입_실패_Email_없음() throws Exception {
        // 유저 등록(이메일 입력X, 실패)
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("");
        userSignupDto.setPassword("asdfqwer");
        userSignupDto.setRoles("ROLE_USER");

        String info = objectMapper.writeValueAsString(userSignupDto);

        mockMvc.perform(post("/signup")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testC_회원가입_실패_PW_없음() throws Exception {
        // 유저 등록(비밀번호 입력X, 실패)
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("b@b.com");
        userSignupDto.setPassword("");
        userSignupDto.setRoles("ROLE_USER");

        String info = objectMapper.writeValueAsString(userSignupDto);

        mockMvc.perform(post("/signup")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testD_회원가입_실패_중복된_Email() throws Exception {
        // 유저 등록 (성공)
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("a@a.com");
        userSignupDto.setPassword("asdfqwer");
        userSignupDto.setRoles("ROLE_USER");

        String info = objectMapper.writeValueAsString(userSignupDto);

        mockMvc.perform(post("/signup")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 같은 유저 재등록 (실패)
        mockMvc.perform(post("/signup")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testE_로그인_성공() throws Exception {
        // 유저 등록 (성공)
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("a@a.com");
        userSignupDto.setPassword("asdfqwer");
        userSignupDto.setRoles("ROLE_USER");

        String info1 = objectMapper.writeValueAsString(userSignupDto);

        mockMvc.perform(post("/signup")
                .content(info1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        // 로그인 (성공)
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("a@a.com");
        userLoginDto.setPassword("asdfqwer");

        String info2 = objectMapper.writeValueAsString(userLoginDto);

        mockMvc.perform(post("/login")
                .content(info2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testF_로그인_실패_Email_입력안함() throws Exception {
        // 유저 등록 (성공)
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("a@a.com");
        userSignupDto.setPassword("asdfqwer");
        userSignupDto.setRoles("ROLE_USER");

        String info1 = objectMapper.writeValueAsString(userSignupDto);

        mockMvc.perform(post("/signup")
                .content(info1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        // 로그인 (이메일 입력X, 실패)
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("");
        userLoginDto.setPassword("asdfqwer");

        String info2 = objectMapper.writeValueAsString(userLoginDto);

        mockMvc.perform(post("/login")
                .content(info2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testG_로그인_실패_PW_입력안함() throws Exception {
        // 유저 등록 (성공)
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("a@a.com");
        userSignupDto.setPassword("asdfqwer");
        userSignupDto.setRoles("ROLE_USER");

        String info1 = objectMapper.writeValueAsString(userSignupDto);

        mockMvc.perform(post("/signup")
                .content(info1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        // 로그인 (비밀번호 입력X, 실패)
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("a@a.com");
        userLoginDto.setPassword("");

        String info2 = objectMapper.writeValueAsString(userLoginDto);

        mockMvc.perform(post("/login")
                .content(info2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testH_로그인_실패_등록된_Email_없음() throws Exception {
        // 유저 등록 (성공)
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("a@a.com");
        userSignupDto.setPassword("asdfqwer");
        userSignupDto.setRoles("ROLE_USER");

        String info1 = objectMapper.writeValueAsString(userSignupDto);

        mockMvc.perform(post("/signup")
                .content(info1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 로그인 (등록된 Email X, 실패)
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("b@a.com");
        userLoginDto.setPassword("asdfqwer");

        String info2 = objectMapper.writeValueAsString(userLoginDto);

        mockMvc.perform(post("/login")
                .content(info2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testH_로그인_실패_잘못된_PW() throws Exception {
        // 유저 등록 (성공)
        UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("a@a.com");
        userSignupDto.setPassword("asdfqwer");
        userSignupDto.setRoles("ROLE_USER");

        String info1 = objectMapper.writeValueAsString(userSignupDto);

        mockMvc.perform(post("/signup")
                .content(info1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 로그인 (비밀번호 오류, 실패)
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("a@a.com");
        userLoginDto.setPassword("asdfqwer1");

        String info2 = objectMapper.writeValueAsString(userLoginDto);

        mockMvc.perform(post("/login")
                .content(info2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
