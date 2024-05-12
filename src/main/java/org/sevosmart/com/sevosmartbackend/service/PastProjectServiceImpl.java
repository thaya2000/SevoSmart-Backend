package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.model.PastProjects;
import org.sevosmart.com.sevosmartbackend.repository.PastProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public PastProjects savePastProject(PastProjects pastProjects) {
        return pastProjectRepository.save(pastProjects);
    }

    @Override
    public PastProjects updatePastProject(String id, PastProjects pastProjects) {
        PastProjects existingProject = pastProjectRepository.findById(id).orElse(null);
        if (existingProject != null) {
            existingProject.setProjectName(pastProjects.getProjectName());
            existingProject.setDescription(pastProjects.getDescription());
            existingProject.setProjectImage(pastProjects.getProjectImage());
            return pastProjectRepository.save(existingProject);
        }
        return null;
    }

    @Override
    public void deletePastProject(String id) {
        pastProjectRepository.deleteById(id);
    }
}
