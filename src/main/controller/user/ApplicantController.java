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
    private static final List<ProjectStatus> ACTIVE_STATUSES = List.of(
        ProjectStatus.SUCCESSFUL,
        ProjectStatus.BOOKED,
        ProjectStatus.PENDING
    );
    
    //Adds a project to the current user's applied projects
    public static void addAppliedProject(Project project) {
        Applicant currentUser = (Applicant) (UserManager.getInstance().getCurrentUser());
        currentUser.addAppliedProject(project);
        UserManager.save();
    }

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
    
    // Checks if an applicant has any active projects (SUCCESSFUL, BOOKED, or PENDING)
    public static boolean hasActiveProject(Applicant applicant) {
        Map<Project, ProjectStatus> appliedProjects = applicant.getAppliedProjects();
        
        for (Map.Entry<Project, ProjectStatus> entry : appliedProjects.entrySet()) {
            if (ACTIVE_STATUSES.contains(entry.getValue())) {
                return true;
            }
        }
        
        return false;
    }
    
    //Counts projects with the specified status for an applicant
    public static int countProjectsByStatus(Applicant applicant, ProjectStatus status) {
        Map<Project, ProjectStatus> appliedProjects = applicant.getAppliedProjects();
        int count = 0;
        
        for (ProjectStatus projStatus : appliedProjects.values()) {
            if (projStatus == status) {
                count++;
            }
        }
        
        return count;
    }
    
    //Counts successful projects for an applicant
    public static int countSuccessfulProject(Applicant applicant) {
        return countProjectsByStatus(applicant, ProjectStatus.SUCCESSFUL);
    }

    //Counts booked projects for an applicant
    public static int countBookedProject(Applicant applicant) {
        return countProjectsByStatus(applicant, ProjectStatus.BOOKED);
    }

    //Counts pending projects for an applicant
    public static int countPendingProject(Applicant applicant) {
        return countProjectsByStatus(applicant, ProjectStatus.PENDING);
    }

    public static Project getSuccessfulProject(Applicant applicant) {
        if (applicant == null) {
            return null;
        }
        
        Map<Project, ProjectStatus> appliedProjects = applicant.getAppliedProjects();
        if (appliedProjects == null || appliedProjects.isEmpty()) {
            return null;
        }
        
        for (Map.Entry<Project, ProjectStatus> entry : appliedProjects.entrySet()) {
            if (entry.getValue() == ProjectStatus.SUCCESSFUL) {
                return entry.getKey();
            }
        }
        
        return null;
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
}