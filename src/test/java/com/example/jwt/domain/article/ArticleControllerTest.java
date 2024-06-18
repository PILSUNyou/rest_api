package com.example.jwt.domain.article;

import com.example.jwt.domain.article.controller.ArticleController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ArticleControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("GET /articles")
    void t1() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/articles")
                )
                .andDo(print());
        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.articles[0].id").exists());
    }

    @Test
    @DisplayName("GET /articles/1")
    void t2() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/articles/1")
                )
                .andDo(print());
        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.articles.id").value(1));
    }

    @Test
    @DisplayName("POST /articles/1")
    @WithUserDetails("user1")
    void t3() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/articles")
                                .content("""
                                        {
                                            "subject" : "제목 new",
                                            "content" : "내용 new"
                                        }
                                        """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());
        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-3"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.articles").exists());
    }

    @Test
    @DisplayName("PATCH /articles/1")
    @WithUserDetails("admin")
    void t4() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        patch("/api/v1/articles/1")
                                .content("""
                                        {
                                            "subject" : "제목 modify1 !",
                                            "content" : "내용 modify1 !"
                                        }
                                        """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());
        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-4"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.articles.id").value(1))
                .andExpect(jsonPath("$.data.articles.subject").value("제목 modify1 !"))
                .andExpect(jsonPath("$.data.articles.content").value("내용 modify1 !"));
    }

    @Test
    @DisplayName("POST /articles/2")
    @WithUserDetails("admin")
    void t5() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(delete("/api/v1/articles/2"))
                .andDo(print());
        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(ArticleController.class))
                .andExpect(handler().methodName("remove"))
                .andExpect(jsonPath("$.resultCode").value("S-5"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.articles.subject",notNullValue()))
                .andExpect(jsonPath("$.data.articles.content",notNullValue()));
    }
}
