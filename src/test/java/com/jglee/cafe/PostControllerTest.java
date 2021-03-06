package com.jglee.cafe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jglee.cafe.category.domain.Category;
import com.jglee.cafe.category.domain.CategoryRepository;
import com.jglee.cafe.post.domain.Post;
import com.jglee.cafe.post.dto.PostDto;
import com.jglee.cafe.post.domain.PostRepository;
import com.jglee.cafe.user.domain.User;
import com.jglee.cafe.user.domain.UserRepository;
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
public class PostControllerTest {

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

    @Before
    public void reset() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        List<String> roles1 = new ArrayList<>();
        roles1.add("ROLE_USER");

        List<String> roles2 = new ArrayList<>();
        roles2.add("ROLE_ADMIN");
        roles2.add("ROLE_USER");

        User user = User.builder()
                .email("author")
                .password("author")
                .roles(roles1)
                .build();

        User admin = User.builder()
                .email("admin")
                .password("admin")
                .roles(roles2)
                .build();

        Category category = Category.builder().name("???????????????").build();

        categoryRepository.save(category);

        userRepository.save(user);
        userRepository.save(admin);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "author", roles = "USER")
    public void testA_?????????_??????_??????() throws Exception {
        //given
        String title = "title";
        String author = "author";
        String content = "content";
        Long category_id = categoryRepository.findAll().get(0).getId();

        LocalDateTime before = LocalDateTime.now().minusHours(1L);
        LocalDateTime after = LocalDateTime.now().plusHours(1L);

        PostDto dto = new PostDto();
        dto.setTitle(title);
        //dto.setAuthor(author);
        dto.setContent(content);
        dto.setCategoryId(category_id);

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
        assertThat(posts.get(0).getCategory().getId()).isEqualTo(category_id);
        assertThat(posts.get(0).getAuthor().getEmail()).isEqualTo(author);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
        assertThat(posts.get(0).getCreatedDate()).isAfter(before);
        assertThat(posts.get(0).getCreatedDate()).isBefore(after);
    }

    @Test
    @WithMockUser(username = "author", roles = "USER")
    public void testB_?????????_??????_??????_??????_??????() throws Exception {
        //given
        String title = "";
        String author = "author";
        String content = "content";
        Long category_id = categoryRepository.findAll().get(0).getId();

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setCategoryId(category_id);

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
    public void testC_?????????_??????_??????_??????_??????() throws Exception {
        //given
        String title = "title";
        String author = "author";
        String content = "content";
        Long category_id = categoryRepository.findAll().get(0).getId();

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setCategoryId(category_id);

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
    public void testD_?????????_?????????_??????_??????() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .category(category)
                .build());

        Thread.sleep(100);

        String title = "title";
        String content = "content";

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setCategoryId(category.getId());

        String info = objectMapper.writeValueAsString(dto);
        Long id = postRepository.findAll().get(0).getId();

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
    public void testE_?????????_?????????_??????_??????() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .category(category)
                .build());

        Thread.sleep(100);

        String title = "title";
        String content = "content";

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setCategoryId(category.getId());

        String info = objectMapper.writeValueAsString(dto);
        Long id = postRepository.findAll().get(0).getId();

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
    public void testF_?????????_??????_??????_??????_??????() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .category(category)
                .build());

        Thread.sleep(100);

        String title = "";
        String content = "content";

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setCategoryId(category.getId());

        String info = objectMapper.writeValueAsString(dto);
        Long id = postRepository.findAll().get(0).getId();

        System.out.println(id);

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
    public void testG_?????????_??????_??????_??????_??????() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .category(category)
                .build());

        Thread.sleep(100);

        String title = "title";
        String content = "content";

        PostDto dto = new PostDto();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setCategoryId(category.getId());

        String info = objectMapper.writeValueAsString(dto);
        Long id = postRepository.findAll().get(0).getId();

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
    public void testH_?????????_?????????_??????_??????() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .category(category)
                .build());

        Long id = postRepository.findAll().get(0).getId();

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
    public void testI_?????????_?????????_??????_??????() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .category(category)
                .build());

        Long id = postRepository.findAll().get(0).getId();

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
    public void testJ_?????????_??????_??????_??????_??????() throws Exception {
        //given
        User author = userRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);

        postRepository.save(Post.builder()
                .title("prev title")
                .content("prev content")
                .author(author)
                .category(category)
                .build());

        Long id = postRepository.findAll().get(0).getId();

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
