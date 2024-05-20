package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.Consultation;
import org.sevosmart.com.sevosmartbackend.repository.GuestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationServiceImpl.class);

    private final GuestRepository guestRepository;

    private final NotificationService notificationService;

    private final GridFsTemplate gridFsTemplate;

    @Value("${max.total.attachment.size}")
    private long MAX_TOTAL_ATTACHMENT_SIZE;

    @Override
    public String saveConsultationDetails(Consultation consultation, List<MultipartFile> attachments) {
        Consultation savedPerson = guestRepository.save(consultation);

        long attachmentTotalSize = calculateTotalSize(attachments);

        if (attachmentTotalSize > MAX_TOTAL_ATTACHMENT_SIZE) {
            return "Total attachment size exceeds the limit";
        }

        if (attachmentTotalSize > 0) {
            saveAttachments(consultation, attachments);
        }

        savedPerson = guestRepository.findById(savedPerson.getGuestId()).orElse(null);
        notificationService.sendConsultationNotification(savedPerson);
        assert savedPerson != null;
        return savedPerson.getGuestId();
    }

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final List<String> ALLOWED_TEXT_EXTENSIONS = Arrays.asList("txt", "pdf", "doc", "docx");

    private void saveAttachments(Consultation consultation, List<MultipartFile> attachments) {
        for (MultipartFile file : attachments) {
            try {
                // Get the file extension
                String originalFilename = file.getOriginalFilename();
                if (originalFilename != null) {
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                            .toLowerCase();

                    // Check if the file extension is allowed
                    if (ALLOWED_IMAGE_EXTENSIONS.contains(fileExtension)
                            || ALLOWED_TEXT_EXTENSIONS.contains(fileExtension)) {
                        String fileId = gridFsTemplate
                                .store(file.getInputStream(), originalFilename, file.getContentType()).toString();
                        consultation.addAttachmentId(fileId);
                    } else {
                        // Handle the case where the file type is not allowed
                        throw new IllegalArgumentException("Invalid file type. Only image and text files are allowed.");
                    }
                }
            } catch (IOException e) {
                logger.error("An error occurred while saving attachments for guest: {}", consultation.getGuestId(), e);
            }
        }
        guestRepository.save(consultation);
    }

    private long calculateTotalSize(List<MultipartFile> attachments) {
        return attachments.stream()
                .mapToLong(MultipartFile::getSize)
                .sum();
    }
}
