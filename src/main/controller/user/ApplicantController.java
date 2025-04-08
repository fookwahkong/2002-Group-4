package main.controller.user;

import java.util.ArrayList;
import java.util.List;

import main.controller.project.ProjectController;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.utils.FileIOUtil;


public class ApplicantController {
    public static void submitEnquiry(String message, Project project) {
        Applicant currentUser = (Applicant) (UserManager.getInstance().getCurrentUser());
        Enquiry enquiry = new Enquiry(currentUser, project, message);
        project.addEnquiry(enquiry);
        // FileIOUtil.saveEnquiryToFile(enquiry, FileIOUtil.ENQUIRIES_FILE);
        System.out.println("Enquiry Submitted.");
    }

    public static void deleteEnquiry(Enquiry enquiry) {
        Applicant applicant = enquiry.getApplicant();
        Project project = enquiry.getProject();

        project.deleteEnquiry(enquiry);
    }

    public static List<Enquiry> getEnquiries() {
        Applicant currentUser = (Applicant) (UserManager.getInstance().getCurrentUser());
        List<Enquiry> returnList = new ArrayList<>();

        List<Project> projectList = ProjectController.getProjectList();
        for (Project p: projectList) {
            List<Enquiry> enquiryList = p.getEnquiries();
            for (Enquiry e: enquiryList) {
                if ((e.getApplicant()).equals(currentUser)) {
                    returnList.add(e);
                }
            }
        }
        return returnList;
    }

    public static void modifyEnquiry(Enquiry enquiry, String newMessage) {
        enquiry.setMessage(newMessage);
        System.out.println("Enquiry updated.");
    }

}
