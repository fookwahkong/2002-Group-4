package controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.project.ProjectController;
import entity.Enquiry;
import entity.project.Project;
import entity.user.Applicant;
import enums.MaritalStatus;
import enums.ProjectStatus;
import utils.FileIOUtil;

public class ApplicantController {

    public static void updateProfile(Project project, String housingType) {
        Applicant applicant = (Applicant) (UserManager.getInstance().getCurrentUser());

        applicant.setBookingDetails(project, housingType);
        FileIOUtil.saveBookingDetails(UserManager.getUsers());
    }
    
    public static boolean checkSingle(Applicant applicant) {
        if (applicant.getAge() >= 35 && applicant.getMaritalStatus() == MaritalStatus.SINGLE) {
            return true;
        } 
        return false;
    }

    /**
     * Checks if an applicant is eligible to apply for a new project
     * Eligibility criteria:
     * 1. Applicant is 35+ and single, OR
     * 2. Applicant is 21+ and married,
     * AND
     * 3. Applicant has no active projects
     */
    public static boolean checkValidity() {
        Applicant applicant = (Applicant) (UserManager.getInstance().getCurrentUser());

        // Check age and marital status first
        if (((applicant.getAge() >= 35 && applicant.getMaritalStatus() == MaritalStatus.SINGLE) ||
                (applicant.getAge() >= 21 && applicant.getMaritalStatus() == MaritalStatus.MARRIED)) &&
                !hasActiveProject(applicant)) {
            return true;
        }

        return false;
    }

    // get projects that associates with the applicant
    public static List<Project> getAppliedProject() {
        Applicant applicant = (Applicant) (UserManager.getInstance().getCurrentUser());
        List<Project> projectList = new ArrayList<>();
        for (Project p : ProjectController.getProjectList()) {
            if (p.getApplicants().contains(applicant)) {
                projectList.add(p);
            }
        }
        return projectList;
    }

    // Submits a new enquiry from the current user
    public static void submitEnquiry(String message, Project project) {
        Applicant currentUser = (Applicant) (UserManager.getInstance().getCurrentUser());
        Enquiry enquiry = new Enquiry(currentUser, project, message);
        project.addEnquiry(enquiry);
        System.out.println("Enquiry Submitted.");
    }

    // Deletes an enquiry

    public static void deleteEnquiry(Enquiry enquiry) {
        Project project = enquiry.getProject();
        project.deleteEnquiry(enquiry);
    }

    // Gets all enquiries for the current user
    public static List<Enquiry> getEnquiries() {
        Applicant currentUser = (Applicant) (UserManager.getInstance().getCurrentUser());
        List<Enquiry> returnList = new ArrayList<>();

        List<Project> projectList = ProjectController.getProjectList();
        for (Project project : projectList) {
            for (Enquiry enquiry : project.getEnquiries()) {
                if (enquiry.getApplicant().equals(currentUser)) {
                    returnList.add(enquiry);
                }
            }
        }

        return returnList;
    }

    // Modifies an existing enquiry
    public static void modifyEnquiry(Enquiry enquiry, String newMessage) {
        enquiry.setMessage(newMessage);
        System.out.println("Enquiry updated.");
    }

    // helper method to check if there is active project
    public static boolean hasActiveProject(Applicant applicant) {
        int activeProjectCount = 0;
        Map<Project, ProjectStatus> applicantActiveProject = ProjectController.getApplicantActiveProject(applicant);
        if (applicantActiveProject == null) {
            return false;
        }
        for (Map.Entry<Project, ProjectStatus> entry : applicantActiveProject.entrySet()) {
            if (entry.getValue() == ProjectStatus.SUCCESSFUL ||
                    entry.getValue() == ProjectStatus.BOOKED ||
                    entry.getValue() == ProjectStatus.PENDING) {
                activeProjectCount++;
            }
        }
        return activeProjectCount > 0;
    }
}