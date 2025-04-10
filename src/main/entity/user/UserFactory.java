package main.entity.user;

import java.util.Map;

import main.entity.project.Project;
import main.enums.MaritalStatus;
import main.enums.ProjectStatus;
import main.enums.UserRole;

public class UserFactory {
    public static User createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole, Map<Project, ProjectStatus> appliedProjects) {
        return switch (userRole) {
            case APPLICANT -> new Applicant(userID, password, name, age, maritalStatus, userRole, appliedProjects);
            case HDB_OFFICER -> new HDBOfficer(userID, password, name, age, maritalStatus, userRole);
            case HDB_MANAGER -> new HDBManager(userID, password, name, age, maritalStatus, userRole);
        };
    }
        
    
}
