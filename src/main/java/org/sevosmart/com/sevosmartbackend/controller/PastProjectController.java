package org.sevosmart.com.sevosmartbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.exception.ProjectNotFoundException;
import org.sevosmart.com.sevosmartbackend.model.PastProjects;
import org.sevosmart.com.sevosmartbackend.repository.PastProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
public class PastProjectController {
    @Autowired
    PastProjectRepository pastProjectRepository;

    @PostMapping(value = "/pastproject", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PastProjects addPastProject(@RequestParam("images") List<MultipartFile> imageFiles,
                                       @RequestParam("name") String name,
                                       @RequestParam("description") String description) throws IOException {
        PastProjects newProject = new PastProjects();
        newProject.setName(name);
        newProject.setDescription(description);

        List<byte[]> imagesData = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            imagesData.add(imageFile.getBytes());
        }

        newProject.setImages(imagesData);
        pastProjectRepository.save(newProject);
        return newProject;
    }

    @GetMapping("/pastprojects")
    List<PastProjects> getAllPastProjects() {
        return pastProjectRepository.findAll();
    }

    @GetMapping("/pastproject/{id}")
    PastProjects getPastProjectById(@PathVariable String id) {
        return pastProjectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }

    @PutMapping(value = "/pastproject/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PastProjects updatePastProject(
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description,
            @PathVariable String id) throws IOException {

        PastProjects existingPastProject = pastProjectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        existingPastProject.setName(name);
        existingPastProject.setDescription(description);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<byte[]> images = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                images.add(imageFile.getBytes());
            }
            existingPastProject.setImages(images);
        }

        return pastProjectRepository.save(existingPastProject);
    }

    @DeleteMapping("/pastproject/{id}")
    String deletePastProject(@PathVariable String id) {
        if (!pastProjectRepository.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        pastProjectRepository.deleteById(id);
        return "Past Project with id " + id + " has been successfully deleted.";
    }
}
