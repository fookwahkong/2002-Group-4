package bto.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bto.controller.enquiry.EnquiryController;
import bto.controller.project.ProjectController;
import bto.entity.Enquiry;
import bto.entity.project.Project;
import bto.entity.user.Applicant;
import bto.enums.MaritalStatus;
import bto.enums.ProjectStatus;
import bto.utils.FileIOUtil;

/**
 * A controller class for applicant operations.
 */
public class ApplicantController {
    
    /**
     * Gets the current applicant user
     * @return The current applicant user
     */
    private static Applicant getCurrentApplicant() {
        return (Applicant) UserManager.getInstance().getCurrentUser();
    }

    /**
     * Updates the applicant's profile
     * @param project The project the applicant is applying for
     * @param housingType The housing type the applicant is applying for
     */
    public static void updateProfile(Project project, Applicant applicant, String housingType) {
        applicant.setBookingDetails(project, housingType);
        FileIOUtil.saveBookingDetails(UserManager.getUsers());
    }
    
    /**
     * Checks if an applicant is single and above 35
     * @param applicant The applicant to check
     * @return true if the applicant is single and above 35, false otherwise
     */
    public static boolean checkSingle(Applicant applicant) {
        return applicant.getAge() >= 35 && applicant.getMaritalStatus() == MaritalStatus.SINGLE;
    }

    /**
     * Checks if an applicant is married and above 21
     * @param applicant The applicant to check
     * @return true if the applicant is married and above 21, false otherwise
     */
    public static boolean checkMarried(Applicant applicant) {
        return applicant.getAge() >= 21 && applicant.getMaritalStatus() == MaritalStatus.MARRIED;
    }

    /**
     * Checks if an applicant is eligible to apply for a new project
     * @return true if the applicant is eligible to apply for a new project, false otherwise
     */
    public static boolean checkEligibility() {
        Applicant applicant = getCurrentApplicant();
        
        boolean meetsAgeAndMaritalStatus = checkSingle(applicant) || checkMarried(applicant);
            
        return meetsAgeAndMaritalStatus && !hasActiveProject(applicant);
    }

    /**
     * Gets all projects that the current applicant has applied for
     * @return List of projects the applicant has applied for
     */
    public static List<Project> getAppliedProjects() {
        Applicant applicant = getCurrentApplicant();
        List<Project> projectList = new ArrayList<>();
        
        for (Project project : ProjectController.getProjectList()) {
            if (project.getApplicants().contains(applicant)) {
                projectList.add(project);
            }
        }
        return projectList;
    }

    /**
     * Submits a new enquiry from the current user
     * @param message The enquiry message
     * @param project The project the enquiry is for
     */
    public static void submitEnquiry(String message, Project project) {
        Applicant currentUser = getCurrentApplicant();
        Enquiry enquiry = new Enquiry(currentUser, project, message);
        project.addEnquiry(enquiry);
        EnquiryController.save();
        System.out.println("Enquiry Submitted.");
    }

    /**
     * Deletes an enquiry
     * @param enquiry The enquiry to delete
     */
    public static void deleteEnquiry(Enquiry enquiry) {
        Project project = enquiry.getProject();
        project.deleteEnquiry(enquiry);
    }

    /**
     * Gets all enquiries for the current user
     * @return List of enquiries made by the current user
     */
    public static List<Enquiry> getEnquiries() {
        Applicant currentUser = getCurrentApplicant();
        List<Enquiry> userEnquiries = new ArrayList<>();

        for (Project project : ProjectController.getProjectList()) {
            for (Enquiry enquiry : project.getEnquiries()) {
                if (enquiry.getApplicant().equals(currentUser)) {
                    userEnquiries.add(enquiry);
                }
            }
        }

        return userEnquiries;
    }

    /**
     * Modifies an enquiry
     * @param enquiry The enquiry to modify
     * @param newMessage The new message
     */
    public static void modifyEnquiry(Enquiry enquiry, String newMessage) {
        enquiry.setMessage(newMessage);
        System.out.println("Enquiry updated.");
    }

    /**
     * Checks if an applicant has an active project
     * @param applicant The applicant to check
     * @return true if the applicant has an active project, false otherwise
     */
    public static boolean hasActiveProject(Applicant applicant) {
        Map<Project, ProjectStatus> applicantActiveProject = ProjectController.getApplicantActiveProject(applicant);
        if (applicantActiveProject == null) {
            return false;
        }
        
        return applicantActiveProject.values().stream()
            .anyMatch(status -> status == ProjectStatus.SUCCESSFUL || 
                              status == ProjectStatus.BOOKED || 
                              status == ProjectStatus.PENDING);
    }
    
    /**
     * Requests a flat booking for an applicant
     * @param project The project to request a flat booking for
     * @param applicant The applicant requesting the flat booking
     * @return true if the request was successful, false otherwise
     */
    public static boolean requestFlatBooking(Project project, Applicant applicant) {
        Map<Project, ProjectStatus> activeProject = ProjectController.getApplicantActiveProject(applicant);
        if (activeProject == null || activeProject.isEmpty()) {
            return false;
        }
        
        ProjectStatus status = activeProject.get(project);
        if (status == ProjectStatus.SUCCESSFUL) {
            ProjectController.updateApplicantStatus(project, applicant, ProjectStatus.REQUEST_BOOK);
            return true;
        }
        
        return false;
    }

    /**
     * Requests a booking withdrawal for an applicant
     * @param project The project to request a booking withdrawal for
     * @param applicant The applicant requesting the booking withdrawal
     * @return true if the request was successful, false otherwise
     */
    public static boolean requestBookingWithdrawal(Project project, Applicant applicant) {
        Map<Project, ProjectStatus> activeProject = ProjectController.getApplicantActiveProject(applicant);
        if (activeProject == null || activeProject.isEmpty()) {
            return false;
        }
        
        ProjectStatus status = activeProject.get(project);
        if (status == ProjectStatus.BOOKED || status == ProjectStatus.REQUEST_BOOK) {
            ProjectController.updateApplicantStatus(project, applicant, ProjectStatus.REQUEST_WITHDRAW);
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets the status of the applicant's active project
     * @param applicant The applicant to get the active project status for
     * @return The status of the applicant's active project
     */
    public static Map<Project, ProjectStatus> getActiveProjectStatus(Applicant applicant) {
        return ProjectController.getApplicantActiveProject(applicant);
    }
}