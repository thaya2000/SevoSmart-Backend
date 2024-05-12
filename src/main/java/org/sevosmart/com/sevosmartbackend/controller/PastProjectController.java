package org.sevosmart.com.sevosmartbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.exception.ProjectNotFoundException;
import org.sevosmart.com.sevosmartbackend.model.PastProjects;
import org.sevosmart.com.sevosmartbackend.repository.PastProjectRepository;
import org.sevosmart.com.sevosmartbackend.service.PastProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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

    @GetMapping("/past-projects/{id}")
    public PastProjects getPastProjectById(@PathVariable String id) {
        return pastProjectService.getPastProjectById(id);
    }

    // @PostMapping(value = "/past-projects", consumes =
    // MediaType.MULTIPART_FORM_DATA_VALUE)
    // public PastProjects savePastProject(@RequestPart("project") PastProjects
    // pastProjects,
    // @RequestPart("file") MultipartFile file) throws IOException {
    // return pastProjectService.savePastProject(file, pastProjects);
    // }

}
