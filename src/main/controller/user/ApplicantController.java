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
        Enquiry enquiry = new Enquiry(currentUser, message);
        project.addEnquiry(enquiry);
        System.out.println("Enquiry Submitted.");
    }
}
