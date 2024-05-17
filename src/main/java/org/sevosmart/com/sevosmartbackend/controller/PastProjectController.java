package org.sevosmart.com.sevosmartbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.PastProjects;
import org.sevosmart.com.sevosmartbackend.service.PastProjectService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin
@RequiredArgsConstructor
public class PastProjectController {

    private final PastProjectService pastProjectService;

    @GetMapping("/past-projects")
    public List<PastProjects> getAllPastProjects() {
        return pastProjectService.getAllPastProjects();
    }

    @GetMapping("/past-project/{id}")
    public PastProjects getPastProjectById(@PathVariable String id) {
        return pastProjectService.getPastProjectById(id);
    }

    @PostMapping(value = "/past-project", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String savePastProject(@ModelAttribute PastProjects pastProjects,
            @RequestPart("Image") List<MultipartFile> Image) throws IOException {
        return pastProjectService.savePastProject(Image, pastProjects);
    }

    @PutMapping(value = "/past-project/{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updatePastProject(@PathVariable("projectId") String projectId,
            @ModelAttribute PastProjects updatedPastProjects,
            @RequestPart("Image") List<MultipartFile> Image) throws IOException {
        return pastProjectService.updatePastProject(projectId, Image, updatedPastProjects);
    }

    @DeleteMapping("/past-project/{projectId}")
    public String deletePastProject(@PathVariable("projectId") String projectId) {
        return pastProjectService.deletePastProject(projectId);
    }

}
