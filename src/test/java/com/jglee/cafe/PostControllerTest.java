package com.jglee.cafe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jglee.cafe.domain.Post;
import com.jglee.cafe.domain.PostRepository;
import com.jglee.cafe.dto.PostDto;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Before
    public void reset() {
        postRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testA_포스트_등록_성공() throws Exception {
        //given
        String title = "title";
        String author = "author";
        String content = "content";
        LocalDateTime before = LocalDateTime.now().minusHours(1L);
        LocalDateTime after = LocalDateTime.now().plusHours(1L);

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setContent(content);

        String info = objectMapper.writeValueAsString(dto);

        //when
        final ResultActions actions = mockMvc.perform(post("/post")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getAuthor()).isEqualTo(author);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
        assertThat(posts.get(0).getCreatedDate()).isAfter(before);
        assertThat(posts.get(0).getCreatedDate()).isBefore(after);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testB_포스트_등록_실패_제목_없음() throws Exception {
        //given
        String title = "";
        String author = "author";
        String content = "content";

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setContent(content);

        String info = objectMapper.writeValueAsString(dto);

        //when
        final ResultActions actions = mockMvc.perform(post("/post")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(roles = "GUEST")
    public void testC_포스트_등록_실패_권한_없음() throws Exception {
        //given
        String title = "title";
        String author = "author";
        String content = "content";

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setContent(content);

        String info = objectMapper.writeValueAsString(dto);

        //when
        final ResultActions actions = mockMvc.perform(post("/post")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(username = "author", roles = "USER")
    public void testD_작성자_포스트_수정_성공() throws Exception {
        //given
        String author = "author";

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .build());

        Thread.sleep(100);

        String title = "title";
        String content = "content";

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setContent(content);

        String info = objectMapper.writeValueAsString(dto);
        Long id = postRepository.findAllByAuthor(author).get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(put("/post/" + id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
        assertThat(posts.get(0).getCreatedDate()).isBefore(posts.get(0).getModifiedDate());
        assertThat(posts.get(0).getModifiedDate()).isAfter(posts.get(0).getCreatedDate());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testE_관리자_포스트_수정_성공() throws Exception {
        //given
        String author = "author";

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .build());

        Thread.sleep(100);

        String title = "title";
        String content = "content";

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setContent(content);

        String info = objectMapper.writeValueAsString(dto);
        Long id = postRepository.findAllByAuthor(author).get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(put("/post/" + id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
        assertThat(posts.get(0).getCreatedDate()).isBefore(posts.get(0).getModifiedDate());
        assertThat(posts.get(0).getModifiedDate()).isAfter(posts.get(0).getCreatedDate());
    }

    @Test
    @WithMockUser(username = "author", roles = "USER")
    public void testF_포스트_수정_실패_제목_없음() throws Exception {
        //given
        String author = "author";

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .build());

        Thread.sleep(100);

        String title = "";
        String content = "content";

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setContent(content);

        String info = objectMapper.writeValueAsString(dto);
        Long id = postRepository.findAllByAuthor(author).get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(put("/post/" + id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo("prev title");
        assertThat(posts.get(0).getContent()).isEqualTo("prev content");
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    public void testG_포스트_수정_실패_권한_없음() throws Exception {
        //given
        String author = "author";

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .build());

        Thread.sleep(100);

        String title = "title";
        String content = "content";

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setContent(content);

        String info = objectMapper.writeValueAsString(dto);
        Long id = postRepository.findAllByAuthor(author).get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(put("/post/" + id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo("prev title");
        assertThat(posts.get(0).getContent()).isEqualTo("prev content");
    }

    @Test
    @WithMockUser(username = "author", roles = "USER")
    public void testH_작성자_포스트_삭제_성공() throws Exception {
        //given
        String author = "author";

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .build());

        Long id = postRepository.findAllByAuthor(author).get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(delete("/post/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testI_관리자_포스트_삭제_성공() throws Exception {
        //given
        String author = "author";

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .build());

        Long id = postRepository.findAllByAuthor(author).get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(delete("/post/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().isOk());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(username = "stranger", roles = "USER")
    public void testJ_포스트_삭제_실패_권한_없음() throws Exception {
        //given
        String author = "author";

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .build());

        Long id = postRepository.findAllByAuthor(author).get(0).getId();

        //when
        final ResultActions actions = mockMvc.perform(delete("/post/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions
                .andExpect(status().is4xxClientError());

        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
    }
}
