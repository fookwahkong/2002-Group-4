package main.controller.user;

import main.entity.user.Applicant;
import main.entity.Enquiry;
import main.entity.project.Project;


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

    public static void viewEnquiries() {
        Applicant currentUser = (Applicant) (UserManager.getInstance().getCurrentUser());
        for (Enquiry enquiry : currentUser.getEnquiryList()) {
            enquiry.viewEnquiry("applicant");
        }
    }

    public static void modifyEnquiry(Enquiry enquiry, String newMessage) {
        enquiry.setMessage(newMessage);
        System.out.println("Enquiry updated.");
    }

}
