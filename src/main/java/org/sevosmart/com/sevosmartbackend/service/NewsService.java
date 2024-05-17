package org.sevosmart.com.sevosmartbackend.service;

import java.util.List;

import org.sevosmart.com.sevosmartbackend.model.News;
import org.springframework.web.multipart.MultipartFile;

public interface NewsService {
    String createNews(News news, MultipartFile image);

    String updateNews(String id, News news, MultipartFile image);

    News getNewsById(String id);

    List<News> getAllNews();



    String deleteNews(String id);
}
