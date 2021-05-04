package com.jglee.cafe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jglee.cafe.domain.Category;
import com.jglee.cafe.domain.CategoryRepository;
import com.jglee.cafe.dto.CategoryDto;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void reset() {
        categoryRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testA_카테고리_생성_성공() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setName("a게시판");

        String info = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/category")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testB_카테고리_생성_실패_카테고리명_없음() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setName("");

        String info = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/category")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testC_카테고리_생성_실패_중복된_카테고리명() throws Exception {
        CategoryDto dto1 = new CategoryDto();
        dto1.setName("a게시판");

        String info1 = objectMapper.writeValueAsString(dto1);

        mockMvc.perform(post("/category")
                .content(info1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        CategoryDto dto2 = new CategoryDto();
        dto2.setName("a게시판");

        String info2 = objectMapper.writeValueAsString(dto2);

        mockMvc.perform(post("/category")
                .content(info2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testD_카테고리_생성_실패_권한_없음() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setName("a게시판");

        String info = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/category")
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testE_카테고리_수정_성공() throws Exception {
        categoryRepository.save(Category.builder()
                .name("a게시판")
                .build());

        String changedName = "b게시판";
        CategoryDto dto = new CategoryDto();
        dto.setName(changedName);

        String info = objectMapper.writeValueAsString(dto);

        Long id = categoryRepository.findByName("a게시판").get().getId();

        mockMvc.perform(put("/category/" + id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories.get(0).getName()).isEqualTo(changedName);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testF_카테고리_수정_실패_카테고리명_없음() throws Exception {
        categoryRepository.save(Category.builder()
                .name("a게시판")
                .build());

        Long id = categoryRepository.findByName("a게시판").get().getId();

        String changedName = "";
        CategoryDto dto = new CategoryDto();
        dto.setName(changedName);

        String info = objectMapper.writeValueAsString(dto);

        List<Category> categories = categoryRepository.findAll();

        //1번은 initial 설정으로 자유게시판으로 했기 때문
        mockMvc.perform(put("/category/"+ id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testG_카테고리_수정_실패_중복된_카테고리명() throws Exception {
        categoryRepository.save(Category.builder()
                .name("a게시판")
                .build());

        Long id = categoryRepository.findByName("a게시판").get().getId();

        String changedName = "a게시판";
        CategoryDto dto = new CategoryDto();
        dto.setName(changedName);

        String info = objectMapper.writeValueAsString(dto);

        List<Category> categories = categoryRepository.findAll();


        //1번은 initial 설정으로 자유게시판으로 했기 때문
        mockMvc.perform(put("/category/"+ id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testH_카테고리_수정_실패_권한_없음() throws Exception {
        categoryRepository.save(Category.builder()
                .name("a게시판")
                .build());

        String changedName = "b게시판";
        CategoryDto dto = new CategoryDto();
        dto.setName(changedName);

        String info = objectMapper.writeValueAsString(dto);

        Long id = categoryRepository.findByName("a게시판").get().getId();

        mockMvc.perform(put("/category/" + id)
                .content(info)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testI_카테고리_삭제_성공() throws Exception {
        categoryRepository.save(Category.builder()
                .name("a게시판")
                .build());

        categoryRepository.save(Category.builder()
                .name("b게시판")
                .build());

        Long id = categoryRepository.findByName("a게시판").get().getId();

        mockMvc.perform(delete("/category/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Category> categories = categoryRepository.findAll();
        Assertions.assertThat(categories.size()).isEqualTo(1);
        Assertions.assertThat(categories.get(0).getName()).isEqualTo("b게시판");
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testJ_카테고리_삭제_실패_권한_없음() throws Exception {
        categoryRepository.save(Category.builder()
                .name("a게시판")
                .build());

        Long id = categoryRepository.findByName("a게시판").get().getId();

        mockMvc.perform(delete("/category/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
