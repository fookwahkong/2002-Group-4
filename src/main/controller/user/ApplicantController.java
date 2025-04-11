package main.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.controller.project.ProjectController;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.enums.MaritalStatus;
import main.enums.ProjectStatus;

public class ApplicantController {
        
    // Active project statuses
    /**
     * Checks if an applicant is eligible to apply for a new project
     * Eligibility criteria:
     * 1. Applicant is 35+ and single, OR
     * 2. Applicant is 21+ and married, 
     * AND
     * 3. Applicant has no active projects
     */
    public static boolean checkValidity(Applicant applicant) {
        // Check age and marital status first
        if (((applicant.getAge() >= 35 && applicant.getMaritalStatus() == MaritalStatus.SINGLE) ||
            (applicant.getAge() >= 21 && applicant.getMaritalStatus() == MaritalStatus.MARRIED)) && 
            !hasActiveProject(applicant)) {
            return true;
        }
        
        return false;
    }


    //Submits a new enquiry from the current user
    public static void submitEnquiry(String message, Project project) {
        Applicant currentUser = (Applicant) (UserManager.getInstance().getCurrentUser());
        Enquiry enquiry = new Enquiry(currentUser, project, message);
        project.addEnquiry(enquiry);
        System.out.println("Enquiry Submitted.");
    }

    //Deletes an enquiry
    
    public static void deleteEnquiry(Enquiry enquiry) {
        Project project = enquiry.getProject();
        project.deleteEnquiry(enquiry);
    }

    //Gets all enquiries for the current user
    
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

    //Modifies an existing enquiry
    public static void modifyEnquiry(Enquiry enquiry, String newMessage) {
        enquiry.setMessage(newMessage);
        System.out.println("Enquiry updated.");
    }

    public static boolean hasActiveProject(Applicant applicant) {
        int activeProjectCount = 0;
        Map<Applicant, ProjectStatus> applicantProjectMap = ProjectController.getApplicantProjectMap();
        for (Map.Entry<Applicant, ProjectStatus> entry : applicantProjectMap.entrySet()) {
            if (entry.getKey().getName().equals(applicant.getName()) && 
            (entry.getValue() == ProjectStatus.SUCCESSFUL || 
            entry.getValue() == ProjectStatus.BOOKED || 
            entry.getValue() == ProjectStatus.PENDING)) {
            activeProjectCount++;
            }
        }
        return activeProjectCount > 0;
    }
}