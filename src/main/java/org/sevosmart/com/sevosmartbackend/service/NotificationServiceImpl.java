package org.sevosmart.com.sevosmartbackend.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.exception.EmailSendingException;
import org.sevosmart.com.sevosmartbackend.model.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

import static org.sevosmart.com.sevosmartbackend.utils.EmailUtils.getEmailMessage;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final GridFsTemplate gridFsTemplate;

    public static final String UTF_8_ENCODING = "UTF-8";

    @Override
    @Async
    public void sendConsultationNotification(Guest guest) {
        try {

            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject("Consultation With Client");
            helper.setFrom("SEVO <" + fromEmail + ">");
            helper.setTo("thevarasathayanan@gmail.com");
            helper.setText(getEmailMessage(guest), false);

            if (guest.getAttachmentIds() != null && !guest.getAttachmentIds().isEmpty()) {
                for (String attachmentId : guest.getAttachmentIds()) {
                    System.out.println("Processing attachmentId: " + attachmentId);

                    // Construct the query and get the GridFSFile
                    Query query = new Query(Criteria.where("_id").is(new String(attachmentId)));
                    GridFSFile gridFSFile = gridFsTemplate.findOne(query);

                    if (gridFSFile != null) {
                        // Use the GridFSFile to get the GridFsResource
                        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);

                        if (resource.exists()) {
                            // Wrap the InputStream using ByteArrayResource
                            ByteArrayResource byteArrayResource = createByteArrayResource(resource.getInputStream());

                            // Attach the file to the email
                            helper.addAttachment(resource.getFilename(), byteArrayResource, resource.getContentType());
                            System.out.println("Attached file for attachmentId: " + attachmentId);
                        } else {
                            System.out.println("File not found for attachmentId: " + attachmentId);
                        }
                    } else {
                        System.out.println("File not found for attachmentId: " + attachmentId);
                    }
                }
            }

            // Send the email outside the loop
            emailSender.send(message);

        } catch (MessagingException | IOException exception) {
            exception.printStackTrace();
            throw new EmailSendingException("Failed to send email with attachments", exception);
        }
    }

    private ByteArrayResource createByteArrayResource(InputStream inputStream) throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        return new ByteArrayResource(bytes);
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }
}
