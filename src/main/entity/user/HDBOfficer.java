package main.entity.user;

import main.controller.project.ProjectController;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.enums.MaritalStatus;
import main.enums.UserRole;
import main.entity.Registration;

import java.util.ArrayList;
import java.util.List;

public class HDBOfficer extends Applicant {
    ArrayList<Project> assignedProjects;
    List<Registration> registrationList;

    public HDBOfficer(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
        this.assignedProjects = new ArrayList<>();
        this.registrationList = new ArrayList<>();
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

    public void viewEnquiriesForProjects() {
        List<Project> officerProjects = ProjectController.getOfficerProjects(this);
        for (Project project : officerProjects) {
            for (Enquiry enquiry : project.getEnquiries()) {
                enquiry.viewEnquiry(getUserRole());
            }
        }
    }

    public List<Project> getAssignedProjects() {
        return assignedProjects;
    }

    public List<Registration> getRegistrationList() {
        return this.registrationList;
    }
    
    public void assignProject(Project project) {
        assignedProjects.add(project);
    }

    public void addRegistration(Registration registration) {
        registrationList.add(registration);
    }
}
