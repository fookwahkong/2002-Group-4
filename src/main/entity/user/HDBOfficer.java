package main.entity.user;

import main.enums.MaritalStatus;
import main.enums.UserRole;

public class HDBOfficer extends Applicant {

    public HDBOfficer(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
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
}
