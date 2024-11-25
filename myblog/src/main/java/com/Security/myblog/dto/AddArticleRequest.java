package com.Security.myblog.dto;

import com.Security.myblog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AddArticleRequest {
    private String title;
    private String content;

    public Article toEntity(String author){
        return Article.builder()
                .author(author)
                .title(title)
                .content(content)
                .build();
    }

}
