package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.model.Consultation;
// import org.springframework.scheduling.annotation.Async;

public interface NotificationService {
    void sendConsultationNotification(Consultation consultation);
}
