package org.sevosmart.com.sevosmartbackend.controller;
import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.Guest;
import org.sevosmart.com.sevosmartbackend.service.GuestService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/notification")
@RequiredArgsConstructor
@CrossOrigin

public class NotificationController {

    private final GuestService guestService;
    @PostMapping
    public String saveGuest(@ModelAttribute Guest guest,
                            @RequestParam("attachments") List<MultipartFile> attachments) {
        return guestService.saveGuestDetails(guest, attachments);
    }
}
