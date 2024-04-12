package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.model.Guest;
// import org.springframework.scheduling.annotation.Async;

public interface NotificationService {
    void sendConsultationNotification(Guest guest);
}
