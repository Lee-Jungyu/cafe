package com.jglee.cafe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jglee.cafe.domain.*;
import com.jglee.cafe.dto.CommentDto;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Before
    public void reset() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        List<String> roles1 = new ArrayList<>();
        roles1.add("ROLE_USER");

        List<String> roles2 = new ArrayList<>();
        roles2.add("ROLE_ADMIN");
        roles2.add("ROLE_USER");

        User author = User.builder()
                .email("author")
                .password("author")
                .roles(roles1)
                .build();

        User admin = User.builder()
                .email("admin")
                .password("admin")
                .roles(roles2)
                .build();

        userRepository.save(author);
        userRepository.save(admin);

        Category category = Category.builder()
                .name("자유게시판")
                .build();

        categoryRepository.save(category);

        Post post = Post.builder()
                .title("title")
                .content("content")
                .author(author)
                .category(category)
                .build();

        postRepository.save(post);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "author", roles = "USER")
    public void testA_코멘트_등록_성공() throws Exception {
        //given
        String content = "content";
        Long postId = postRepository.findAll().get(0).getId();
        String author = "author";

        CommentDto dto = new CommentDto();
        dto.setAuthor(author);
        dto.setPostId(postId);
        dto.setContent(content);

        String info = objectMapper.writeValueAsString(dto);

        //when
        final ResultActions actions = mockMvc.perform(post("/comment")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContent()).isEqualTo(content);
        assertThat(comments.get(0).getAuthor().getEmail()).isEqualTo(author);
        assertThat(comments.get(0).getPost().getId()).isEqualTo(postId);
    }

    @Test
    @WithMockUser(username = "author", roles = "USER")
    public void testB_코멘트_등록_실패_내용_없음() throws Exception {
        //given
        String content = "";
        Long postId = postRepository.findAll().get(0).getId();
        String author = "author";

        CommentDto dto = new CommentDto();
        dto.setAuthor(author);
        dto.setPostId(postId);
        dto.setContent(content);

        String info = objectMapper.writeValueAsString(dto);

        //when
        final ResultActions actions = mockMvc.perform(post("/comment")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(roles = "GUEST")
    public void testC_코멘트_등록_실패_권한_없음() throws Exception {
        //given
        String content = "content";
        Long postId = postRepository.findAll().get(0).getId();
        String author = "author";

        CommentDto dto = new CommentDto();
        dto.setAuthor(author);
        dto.setPostId(postId);
        dto.setContent(content);

        String info = objectMapper.writeValueAsString(dto);

        //when
        final ResultActions actions = mockMvc.perform(post("/comment")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(username = "author", roles = "USER")
    public void testD_코멘트_작성자_수정_성공() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Post post = postRepository.findAll().get(0);

        commentRepository.save(Comment.builder()
                .content("prev content")
                .author(author)
                .post(post)
                .build());

        Thread.sleep(100);

        String content = "content";

        CommentDto dto = new CommentDto();
        dto.setContent(content);
        dto.setAuthor(author.getEmail());
        dto.setPostId(post.getId());

        String info = objectMapper.writeValueAsString(dto);
        Long id = commentRepository.findAll().get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(put("/comment/" + id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.get(0).getContent()).isEqualTo(content);
        assertThat(comments.get(0).getCreatedDate()).isBefore(comments.get(0).getModifiedDate());
        assertThat(comments.get(0).getModifiedDate()).isAfter(comments.get(0).getCreatedDate());
    }

    @Test
    @WithMockUser(username = "author", roles = "USER")
    public void testE_코멘트_수정_실패_내용_없음() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Post post = postRepository.findAll().get(0);

        commentRepository.save(Comment.builder()
                .content("prev content")
                .author(author)
                .post(post)
                .build());

        Thread.sleep(100);

        String content = "";

        CommentDto dto = new CommentDto();
        dto.setContent(content);
        dto.setAuthor(author.getEmail());
        dto.setPostId(post.getId());

        String info = objectMapper.writeValueAsString(dto);
        Long id = commentRepository.findAll().get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(put("/comment/" + id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.get(0).getContent()).isEqualTo("prev content");
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    public void testF_코멘트_수정_실패_권한_없음() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Post post = postRepository.findAll().get(0);

        commentRepository.save(Comment.builder()
                .content("prev content")
                .author(author)
                .post(post)
                .build());

        Thread.sleep(100);

        String content = "content";

        CommentDto dto = new CommentDto();
        dto.setContent(content);
        dto.setAuthor(author.getEmail());
        dto.setPostId(post.getId());

        String info = objectMapper.writeValueAsString(dto);
        Long id = commentRepository.findAll().get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(put("/comment/" + id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.get(0).getContent()).isEqualTo("prev content");
    }

    @Test
    @WithMockUser(username = "author", roles = "USER")
    public void testG_코멘트_작성자_삭제_성공() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Post post = postRepository.findAll().get(0);

        commentRepository.save(Comment.builder()
                .content("content")
                .author(author)
                .post(post)
                .build());

        Long id = commentRepository.findAll().get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(delete("/comment/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testH_코멘트_관리자_삭제_성공() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Post post = postRepository.findAll().get(0);

        commentRepository.save(Comment.builder()
                .content("content")
                .author(author)
                .post(post)
                .build());

        Long id = commentRepository.findAll().get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(delete("/comment/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    public void testI_코멘트_삭제_실패_권한_없음() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Post post = postRepository.findAll().get(0);

        commentRepository.save(Comment.builder()
                .content("content")
                .author(author)
                .post(post)
                .build());

        Long id = commentRepository.findAll().get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(delete("/comment/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(1);
    }
}
