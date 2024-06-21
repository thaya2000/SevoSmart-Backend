package org.sevosmart.com.sevosmartbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.service.ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile) {
        return imageService.upload(multipartFile);
    }


    @DeleteMapping("/delete")
    public void delete(@RequestParam("fileUrl") String fileUrl) {
        imageService.delete(fileUrl);
    }

    @PutMapping("/edit")
    public String edit(@RequestParam("newFile") MultipartFile newFile, @RequestParam("oldFileUrl") String oldFileUrl) {
        return imageService.edit(newFile, oldFileUrl);
    }
}
