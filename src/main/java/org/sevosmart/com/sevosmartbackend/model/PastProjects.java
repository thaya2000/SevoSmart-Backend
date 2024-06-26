package org.sevosmart.com.sevosmartbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pastProjects")
public class PastProjects {
    @Id
    private String projectId;
    private String projectName;
    private List<byte[]> projectImages;
    private List<String> productImageURL;
    private String description;
}
