package org.sevosmart.com.sevosmartbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private byte[] projectImage;
    private String description;
}
