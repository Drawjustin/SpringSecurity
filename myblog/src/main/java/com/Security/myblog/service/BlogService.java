package com.Security.myblog.service;

import com.Security.myblog.domain.Article;
import com.Security.myblog.dto.AddArticleRequest;
import com.Security.myblog.dto.UpdateArticleRequest;
import com.Security.myblog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request, String userName){
        return blogRepository.save(request.toEntity(userName));
    }

        public Article findById(long id){
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found " +  id));
    }
    public void delete(long id){
        Article article = blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }



    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found : "+ id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(),request.getContent());
        return article;
    }

    private static void authorizeArticleAuthor(Article article){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!article.getAuthor().equals(userName)){
            throw new IllegalArgumentException("not authorized");
        }
    }
    public List<Article> findAll(){
        return blogRepository.findAll();
    }






    // Oauth 사용 이전 코드

//        public Article save(AddArticleRequest request, String userName){
//        return blogRepository.save(request.toEntity(userName));
//    }
//
//    public List<Article> findAll(){
//        return blogRepository.findAll();
//    }
//    public Article findById(long id){
//        return blogRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found " +  id));
//    }
//
//    public void delete(long id){
//        blogRepository.deleteById(id);
//    }
//
//    @Transactional
//    public Article update(long id, UpdateArticleRequest request) {
//        Article article = blogRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
//
//        article.update(request.getTitle(), request.getContent());
//
//        return article;
//    }

}
