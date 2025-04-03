package main.entity.user;

import java.util.*;

import main.enums.MaritalStatus;
import main.enums.UserRole;
import main.controller.project.ProjectController;
import main.entity.Enquiry;
import main.entity.project.Project;

public class HDBOfficer extends Applicant {
    ArrayList<Project> assignedProjects;
    
    public HDBOfficer(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
        this.assignedProjects = new ArrayList<>();
    }

    public void viewEnquiriesForProjects() {
        List<Project> officerProjects = ProjectController.getOfficerProjects(this);
        for (Project project : officerProjects) {
            for (Enquiry enquiry : project.getEnquiries()) {
                enquiry.viewEnquiry("officer");
            }
        }
    }

    public static void replyToEnquiry(Enquiry enquiry, String response) {
        if (enquiry == null) {
            System.out.println("Invalid enquiry.");
            return;
        }
        enquiry.setResponse(response);
        enquiry.setReplied(true);  
        System.out.println("Response submitted: " + response);
    }

    public List<Project> getAssignedProjects() {
        return assignedProjects;
    }

    public void assignProject(Project project) {
        assignedProjects.add(project);
    }
}
