package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.model.Guest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface GuestService {
    String saveGuestDetails(Guest guest, List<MultipartFile> attachments);
}
