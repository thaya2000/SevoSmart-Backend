package org.sevosmart.com.sevosmartbackend.exception;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException(String id) {
        super("Could not found the Past project with id " + id);

    }
}
