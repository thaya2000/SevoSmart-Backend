package org.sevosmart.com.sevosmartbackend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "consultation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Consultation {
    @Id
    private String guestId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String address;
    private String category;
    private String chooseProduct;
    private String message;
    private List<String> attachmentIds;

    public void addAttachmentId(String attachmentId) {
        if (attachmentIds == null) {
            attachmentIds = new ArrayList<>();
        }
        attachmentIds.add(attachmentId);
    }

}
