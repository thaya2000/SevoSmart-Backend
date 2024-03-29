package org.sevosmart.com.sevosmartbackend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "guest")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Guest {
    @Id
    private String guestId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private List<String> attachmentIds;

    public void addAttachmentId(String attachmentId) {
        if (attachmentIds == null) {
            attachmentIds = new ArrayList<>();
        }
        attachmentIds.add(attachmentId);
    }

}
