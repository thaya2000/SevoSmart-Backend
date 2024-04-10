package org.sevosmart.com.sevosmartbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import javax.persistence.ElementCollection;
import javax.persistence.Lob;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class PastProjects {
    @Id
    private String id;
    private String name;
    @Lob
    @ElementCollection
    private List<byte[]> images;

    private String description;
}
