package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.News;
import org.sevosmart.com.sevosmartbackend.repository.NewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Override
    public String createNews(News news, MultipartFile image) {
        try {
            news.setNewsImage(image.getBytes());
            News createdNews = newsRepository.save(news);
            return "News created with ID: " + createdNews.getNewsId();
        } catch (Exception e) {
            return "Error creating news: " + e.getMessage();
        }
    }

    @Override
    public String updateNews(String id, News news, MultipartFile image) {
        try {
            Optional<News> existingNews = newsRepository.findById(id);
            if (existingNews.isPresent()) {
                News updatedNews = existingNews.get();
                updatedNews.setNewsTitle(news.getNewsTitle());
                updatedNews.setNewsContent(news.getNewsContent());
                updatedNews.setNewsImage(image.getBytes());
                newsRepository.save(updatedNews);
                return "News updated with ID: " + updatedNews.getNewsId();
            } else {
                return "News with ID: " + id + " not found";
            }
        } catch (Exception e) {
            return "Error updating news: " + e.getMessage();
        }
    }

    @Override
    public News getNewsById(String id) {
        Optional<News> news = newsRepository.findById(id);
        return news.orElse(null);
    }

    @Override
    public List<News> getAllNews() {
        try {
            return newsRepository.findAll();
        } catch (Exception e) {
            return List.of();
        }
    }



    @Override
    public String deleteNews(String id) {
        try {
            newsRepository.deleteById(id);
            return "News deleted with ID: " + id;
        } catch (Exception e) {
            return "Error deleting news: " + e.getMessage();
        }
    }
}
