package com.example.jwt.domain.article.controller;

import com.example.jwt.domain.article.entity.Article;
import com.example.jwt.domain.article.service.ArticleService;
import com.example.jwt.domain.member.entity.Member;
import com.example.jwt.domain.member.service.MemberService;
import com.example.jwt.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;

    @AllArgsConstructor
    @Getter
    public static class ArticlesResponse{
        private final List<Article> articles;
    }

    @GetMapping(value = "")
    @Operation(summary = "다건조회")
    public RsData<ArticlesResponse> articles(){
        List<Article> articles = articleService.findAll();

        return RsData.of(
                "S-1",
                "성공",
                new ArticlesResponse(articles)
        );
    }

    @AllArgsConstructor
    @Getter
    public static class ArticleResponse{
        private final Article article;
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "단건조회")
    public RsData<ArticleResponse> article(@PathVariable("id") Long id){
        return articleService.findById(id).map(article -> RsData.of(
                "S-1",
                "성공",
                new ArticleResponse(article)
        )).orElseGet(() -> RsData.of(
                "F-1",
                "@d번 게시물은 존재하지 않습니다.".formatted(id),
                null
        ));
    }

    @Data
    public static class WriteRequest{
        @NotBlank
        private String subject;
        @NotBlank
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class WriteResponse {
        private final Article article;
    }

    @PostMapping(value = "")
    @Operation(summary = "등록", security = @SecurityRequirement(name = "bearerAuth"))
    public RsData<WriteResponse> write(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody WriteRequest writeRequest
            ){
        Member member = memberService.findByUsername(user.getUsername()).orElseThrow();
        RsData<Article> writeRs = articleService.write(member, writeRequest.getSubject(), writeRequest.getContent());

        if (writeRs.isFail()) return (RsData) writeRs;

        return RsData.of(
                writeRs.getResultCode(),
                writeRs.getMsg(),
                new WriteResponse(writeRs.getData())
        );
    }
}
