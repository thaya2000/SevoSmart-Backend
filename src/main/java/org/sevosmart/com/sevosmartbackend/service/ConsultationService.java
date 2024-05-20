package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.model.Consultation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ConsultationService {
    String saveConsultationDetails(Consultation consultation, List<MultipartFile> attachments);
}
