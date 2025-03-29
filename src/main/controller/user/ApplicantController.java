package main.controller.user;

import main.entity.user.Applicant;
import main.entity.user.User;
import main.entity.project.Project;
import main.controller.user.UserManager;
import main.entity.project.Enquiry;

import java.util.Scanner;

public class ApplicantController {
    public static void submitEnquiry(String message, Project project) {
        Applicant currentUser = (Applicant) (UserManager.getInstance().getCurrentUser());
        Enquiry enquiry = new Enquiry(currentUser, project, message);
        project.addEnquiry(enquiry);
        currentUser.addEnquiry(enquiry);
        System.out.println("Enquiry Submitted.");
    }

    public static void deleteEnquiry(Enquiry enquiry) {
        Applicant applicant = enquiry.getApplicant();
        Project project = enquiry.getProject();

        applicant.deleteEnquiry(enquiry);
        project.deleteEnquiry(enquiry);
    }
}
