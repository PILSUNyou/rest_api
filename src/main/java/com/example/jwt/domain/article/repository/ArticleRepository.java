package com.example.jwt.domain.article.repository;

import com.example.jwt.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArticleRepository extends JpaRepository<Article, Long> {

}
