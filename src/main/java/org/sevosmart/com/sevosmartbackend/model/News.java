package org.sevosmart.com.sevosmartbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "news")
public class News {
    @Id
    private String newsId;
    private String newsTitle;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate newsPublishDate;
    private byte[] newsImage;
    private String newsContent;
}
