package com.Security.myblog.dto;

import com.Security.myblog.domain.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ArticleViewResponse {
    private Long id;
    private String author;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public ArticleViewResponse(Article article){
        this.id = article.getId();
        this.author = article.getAuthor();
        this.content = article.getContent();
        this.title = article.getTitle();
        this.createdAt = article.getCreatedAt();
    }
}
