package main.entity.user;

import java.util.HashMap;
import java.util.Map;

import main.entity.project.Project;
import main.enums.MaritalStatus;
import main.enums.UserRole;
import main.enums.ProjectStatus;

public class Applicant extends User {
    Map<Project, ProjectStatus> appliedProjects;
    private String rawAppliedProjectStr;

    public Applicant(String userID, String password, String name, int age, MaritalStatus maritalStatus,
            UserRole userRole, Map<Project, ProjectStatus> appliedProjects) {
        super(userID, password, name, age, maritalStatus, userRole);
                
        this.appliedProjects = appliedProjects != null ? appliedProjects : new HashMap<>();
    }

    public String getName() {
        return super.getName();
    }

    public int getAge() {
        return super.getAge();
    }

    public Map<Project, ProjectStatus> getAppliedProjects() {
        return this.appliedProjects;
    }

    public void addAppliedProject(Project project) {
        appliedProjects.put(project, ProjectStatus.PENDING); //by default, put PENDING unless manager changes it
    }

    public void setRawAppliedProjectsStr(String rawStr) {
        this.rawAppliedProjectStr = rawStr;
    }

    public String getRawAppliedProjectStr() {
        return this.rawAppliedProjectStr;
    }
}
