package main.entity.user;

import main.controller.project.ProjectController;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.enums.MaritalStatus;
import main.enums.UserRole;

import java.util.List;

public class HDBOfficer extends User {

    public HDBOfficer(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    //do we need this
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
}
