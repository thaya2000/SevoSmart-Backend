package org.sevosmart.com.sevosmartbackend.controller;
import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.Consultation;
import org.sevosmart.com.sevosmartbackend.service.ConsultationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/notification")
@RequiredArgsConstructor
@CrossOrigin

public class ConsultationController {

    private final ConsultationService consultationService;
    @PostMapping
    public String saveConsultationDetails(@ModelAttribute Consultation consultation,
                                          @RequestParam("attachments") List<MultipartFile> attachments) {
        return consultationService.saveConsultationDetails(consultation, attachments);
    }
}
