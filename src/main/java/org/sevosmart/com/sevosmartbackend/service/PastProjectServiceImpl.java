package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.PastProjects;
import org.sevosmart.com.sevosmartbackend.repository.PastProjectRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PastProjectServiceImpl implements PastProjectService {

    private final PastProjectRepository pastProjectRepository;

    @Override
    public List<PastProjects> getAllPastProjects() {
        return pastProjectRepository.findAll();
    }

    @Override
    public PastProjects getPastProjectById(String id) {
        return pastProjectRepository.findById(id).orElse(null);
    }

    @Override
    public String savePastProject(MultipartFile file, PastProjects pastProjects) {
        try {
            pastProjects.setProjectImage(file.getBytes());
            pastProjectRepository.save(pastProjects);
            return "Project saved successfully";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String updatePastProject(String projectId, MultipartFile file, PastProjects updatedPastProjects) {
        try {
            Optional<PastProjects> existingProjectOpt = pastProjectRepository.findById(projectId);
            if (existingProjectOpt.isPresent()) {
                PastProjects existingProject = existingProjectOpt.get();
                existingProject.setProjectName(updatedPastProjects.getProjectName());
                existingProject.setDescription(updatedPastProjects.getDescription());
                if (file != null) {
                    existingProject.setProjectImage(file.getBytes());
                }
                pastProjectRepository.save(existingProject);
                return "Project updated successfully";
            } else {
                return "Project not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deletePastProject(String id) {
        try {
            pastProjectRepository.deleteById(id);
            return "Delete successful";
        } catch (EmptyResultDataAccessException e) {
            return "Project not found";
        }
    }
}
