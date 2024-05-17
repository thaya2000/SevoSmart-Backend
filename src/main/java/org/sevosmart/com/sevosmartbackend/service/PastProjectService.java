package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.model.PastProjects;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PastProjectService {
    List<PastProjects> getAllPastProjects();

    PastProjects getPastProjectById(String id);

    String savePastProject(List<MultipartFile> file, PastProjects pastProjects);

    String updatePastProject(String projectId, List<MultipartFile> file, PastProjects updatedPastProjects);

    String deletePastProject(String id);
}
