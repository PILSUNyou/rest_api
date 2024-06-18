package com.example.jwt.domain.article.service;

import com.example.jwt.domain.article.entity.Article;
import com.example.jwt.domain.article.repository.ArticleRepository;
import com.example.jwt.domain.member.entity.Member;
import com.example.jwt.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private  final ArticleRepository articleRepository;

    public RsData<Article> write(Member author, String subject, String content){
        Article article = Article.builder()
                .author(author)
                .subject(subject)
                .content(content)
                .build();

        articleRepository.save(article);

        return RsData.of(
                "S-3",
                "게시물이 생성 되었습니다.",
                article
        );
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
