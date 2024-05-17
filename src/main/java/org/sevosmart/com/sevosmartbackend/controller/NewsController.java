package org.sevosmart.com.sevosmartbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.exception.NewsNotFoundException;
import org.sevosmart.com.sevosmartbackend.model.News;
import org.sevosmart.com.sevosmartbackend.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/news")
@CrossOrigin
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createNews(@ModelAttribute News news, @RequestPart("Image") MultipartFile image) {
        try {
            String createdNews = newsService.createNews(news, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value ="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateNews(@PathVariable String id, @ModelAttribute News news, @RequestPart("Image") MultipartFile image) {
        try {
            String updatedNews = newsService.updateNews(id, news, image);
            return ResponseEntity.ok(updatedNews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable String id) {
        News news = newsService.getNewsById(id);
        if (news == null) {
            throw new NewsNotFoundException("News with id " + id + " not found");
        }
        return ResponseEntity.ok(news);
    }

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> newsList = newsService.getAllNews();
        if (newsList.isEmpty()) {
            throw new NewsNotFoundException("No news found");
        }
        return ResponseEntity.ok(newsList);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable String id) {
        String deleteMessage = newsService.deleteNews(id);
        return ResponseEntity.ok(deleteMessage);
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<String> handleNewsNotFoundException(NewsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
