package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.model.PastProjects;

import java.util.List;

public interface PastProjectService {
    List<PastProjects> getAllPastProjects();

    PastProjects getPastProjectById(String id);

    PastProjects savePastProject(PastProjects pastProjects);

    PastProjects updatePastProject(String id, PastProjects pastProjects);

    void deletePastProject(String id);
}
