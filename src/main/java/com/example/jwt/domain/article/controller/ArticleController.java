package com.example.jwt.domain.article.controller;

import com.example.jwt.domain.article.entity.Article;
import com.example.jwt.domain.article.service.ArticleService;
import com.example.jwt.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/vi/articles")
public class ArticleController {

    private final ArticleService articleService;

    @AllArgsConstructor
    @Getter
    public static class ArticleResponse{
        private final List<Article> articles;
    }

    @GetMapping(value = "")
    @Operation(summary = "게시물들")
    public RsData<ArticleResponse> articles(){
        List<Article> articles = articleService.findAll();

        return RsData.of(
                "S-1",
                "성공",
                new ArticleResponse(articles)
        );
    }
}
