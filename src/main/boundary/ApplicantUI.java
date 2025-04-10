package main.boundary;

import main.controller.project.ProjectController;
import main.controller.user.ApplicantController;
import main.controller.user.OfficerController;
import main.controller.user.UserManager;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.entity.user.User;
import main.enums.ProjectStatus;
import main.enums.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApplicantUI extends UI {
    private final Applicant currentUser;
    private ChangePasswordUI changePasswordUI = new ChangePasswordUI();

    public ApplicantUI() {
        User user = UserManager.getInstance().getCurrentUser();

        // downcasting user to applicant
        if (user != null && (user.getUserRole() == UserRole.APPLICANT )) {
            this.currentUser = (Applicant) user;
        } else {
            throw new IllegalStateException("Current user is not an Applicant");
        }
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            displayMenuOptions();

            try {
                int choice = getValidIntInput(1, 11);

                switch (choice) {
                    case 1 -> viewOpenProjects();
                    case 2 -> applyProject();
                    case 3 -> viewAppliedProjects();
                    case 4 -> {System.out.println("Option 4 is not implemented yet");} //flatBooking();
                    case 5 -> {System.out.println("Option 5 is not implemented yet");}
                    case 6 -> submitEnquiry();
                    case 7 -> viewEnquiry();
                    case 8 -> editEnquiry();
                    case 9 -> deleteEnquiry();
                    case 10 -> changePasswordUI.showChangePasswordMenu();
                    case 11 -> {
                        UserManager.getInstance().logout();
                        running = false;
                        new LoginUI().startLogin();
                    }
                    default -> System.out.println("Invalid choice! Please enter a number between 1 and 5");
                }

            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void displayMenuOptions() {
        String[] menuOptions = {
            "APPLICANT UI",
            "==================================",
            "1. View Open Projects",
            "2. Apply Project",
            "3. View Applied Projects",
            "4. Book flat through HDB Officer (Reserved)",
            "5. (Reserved)",
            "6. Submit Enquiry",
            "7. View Submitted Enquiry",
            "8. Edit Submitted Enquiry",
            "9. Delete Enquiry",
            "10. Change Password",
            "11. Logout",
            "==================================",
            "Enter your choice: "
        };

        for (String option : menuOptions) {
            System.out.println(option);
        }
    }

    //option 1
    protected void viewOpenProjects() {
        System.out.println("List of Open Projects: ");
        List<Project> projectList = ProjectController.getApplicantProjects(currentUser);

        if (projectList.isEmpty()) {
            System.out.println("No visible project to view");
            return;
        }
        displayProjectList(projectList);
        try {
            int projIndex = getIntInput("Select the project to view details for: ") - 1;
            Project project = projectList.get(projIndex);

            if (project != null) {
                ProjectController.displayProjectDetails(project);
            }
        } catch (Exception e) {
            System.out.println("Error viewing project: " + e.getMessage());
        }
    }

    //option 2
    protected void applyProject() {
        try {
            boolean validForApply = ApplicantController.checkValidity(currentUser);
            if (!validForApply) {
                System.out.println("You are not eligible to apply for a project at this time.");
                return;
            }
            
            List<Project> projectList = ProjectController.getApplicantProjects(currentUser);

            if (projectList == null || projectList.isEmpty()) {
                System.out.println("Sorry. Currently no projects available for you to apply for.");
                return;
            }

            System.out.println("List of Projects you can apply for: ");
            System.out.println("=====================================");
            displayProjectList(projectList);

            int projIndex = getIntInput("Select the project you want to apply for (0 to cancel): ") - 1;
            
            if (projIndex == -1) {
                System.out.println("Application cancelled.");
                return;
            }
            
            if (projIndex < 0 || projIndex >= projectList.size()) {
                System.out.println("Invalid project selection.");
                return;
            }
            
            Project proj = projectList.get(projIndex);
            if (proj == null) {
                System.out.println("Error: Selected project is null.");
                return;
            }

            ProjectController.addApplicants(proj, currentUser);
            ApplicantController.addAppliedProject(proj);
            System.out.println("Application successful!");
        } catch (Exception e) {
            System.out.println("Error applying for project: " + e.getMessage());
        }
    }

    //option 3
    protected void viewAppliedProjects() {
        try {
            Map<Project, ProjectStatus> projectSet = currentUser.getAppliedProjects();

            if (projectSet == null || projectSet.isEmpty()) {
                System.out.println("You haven't applied for any projects yet!");
                return;
            }

            List<Project> projectList = new ArrayList<>(projectSet.keySet());
            System.out.println("List of Projects you have applied for: ");
            int cnt = 1;
            for (Project p : projectList) {
                if (p != null) {
                    System.out.print(cnt + ". ");
                    ProjectStatus status = projectSet.get(p);
                    String statusStr = (status != null) ? status.toString() : "UNKNOWN";
                    String visibility = (p.getVisibility() == true) ? "Open" : "Closed";
                    System.out.println(p.getName() + ": " + statusStr + ", Visibility: " + visibility);
                    cnt += 1;
                }
            }

            int projIndex = getIntInput("Select the project you want to view (0 to cancel): ") - 1;
            
            if (projIndex == -1) {
                System.out.println("Operation cancelled.");
                return;
            }
            
            if (projIndex < 0 || projIndex >= projectList.size()) {
                System.out.println("Invalid project selection.");
                return;
            }
            
            Project proj = projectList.get(projIndex);
            if (proj == null) {
                System.out.println("Error: Selected project is null.");
                return;
            }
            
            ProjectStatus projStatus = projectSet.get(proj);
            
            if (projStatus == null) {
                System.out.println("Unknown project status.");
                return;
            }
            
            switch (projStatus) {
                case PENDING -> System.out.println("The application is still pending");
                case SUCCESSFUL -> System.out.println("The application is successful.");
                case UNSUCCESSFUL -> System.out.println("The application is unsuccessful.");
                case BOOKED -> System.out.println("The project is successfully applied and a flat has been booked.");
                default -> System.out.println("Unknown project status.");
            }
        } catch (Exception e) {
            System.out.println("Error viewing applied projects: " + e.getMessage());
        }
    }

    //Option 4: Flat Booking
    protected void flatBooking() {
        try {
            int successfulCount = ApplicantController.countSuccessfulProject(currentUser);
            
            if (successfulCount == 0) {
                System.out.println("You have no successful application yet!");
                return;
            }
            
            Project project = ApplicantController.getSuccessfulProject(currentUser);
            
            System.out.println("Project: " + project.getName() + " is available for booking.");
            int choice = getIntInput("1 to book, 0 to cancel");
            
            //booking logic is here

        } catch (Exception e) {
            System.out.println("Error with flat booking: " + e.getMessage());
        }
    }

    //option 6
    protected void submitEnquiry() {
        try {
            System.out.println("List of visible projects:");
            List<Project> projectList = ProjectController.getProjectList();
            
            if (projectList == null || projectList.isEmpty()) {
                System.out.println("No projects available to submit enquiries for.");
                return;
            }
            
            displayProjectList(projectList);

            int projIndex = getIntInput("Select the project to submit enquiry for (0 to cancel): ") - 1;
            
            if (projIndex == -1) {
                System.out.println("Enquiry submission cancelled.");
                return;
            }
            
            if (projIndex < 0 || projIndex >= projectList.size()) {
                System.out.println("Invalid project selection.");
                return;
            }
            
            Project proj = projectList.get(projIndex);

            System.out.print("Enter enquiry message: ");
            String message = scanner.nextLine();
            
            if (message == null || message.trim().isEmpty()) {
                System.out.println("Enquiry message cannot be empty.");
                return;
            }

            ApplicantController.submitEnquiry(message, proj);
        } catch (Exception e) {
            System.out.println("Error submitting enquiry: " + e.getMessage());
        }
    }

    //option 7
    protected void viewEnquiry() {
        try {
            List<Enquiry> enquiryList = ApplicantController.getEnquiries();

            if (enquiryList == null || enquiryList.isEmpty()) {
                System.out.println("You haven't submitted any enquiries yet.");
                return;
            }

            System.out.println("Your Enquiries:");
            int cnt = 0;
            for (Enquiry e: enquiryList) {
                if (e != null) {
                    cnt += 1;
                    System.out.print(cnt + ". ");
                    e.viewEnquiry(currentUser.getUserRole());
                }
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error viewing enquiries: " + e.getMessage());
        }
    }

    //option 8
    protected void editEnquiry() {
        try {
            List<Enquiry> enquiryList = ApplicantController.getEnquiries();
            
            if (enquiryList == null || enquiryList.isEmpty()) {
                System.out.println("You don't have any enquiries to edit.");
                return;
            }
            
            System.out.println("Your Enquiries:");
            int cnt = 0;
            for (Enquiry e: enquiryList) {
                if (e != null) {
                    cnt += 1;
                    System.out.print(cnt + ". ");
                    e.viewEnquiry(currentUser.getUserRole());
                }
            }
            System.out.println();

            int enquiryIndex = getIntInput("Select the enquiry to edit (0 to cancel): ") - 1;
            
            if (enquiryIndex == -1) {
                System.out.println("Edit operation cancelled.");
                return;
            }
            
            if (enquiryIndex < 0 || enquiryIndex >= enquiryList.size()) {
                System.out.println("Invalid enquiry selection.");
                return;
            }
            
            Enquiry enquiry = enquiryList.get(enquiryIndex);

            System.out.print("Enter new enquiry message: ");
            String newMessage = scanner.nextLine();
            
            if (newMessage == null || newMessage.trim().isEmpty()) {
                System.out.println("New message cannot be empty.");
                return;
            }
            
            ApplicantController.modifyEnquiry(enquiry, newMessage);
        } catch (Exception e) {
            System.out.println("Error editing enquiry: " + e.getMessage());
        }
    }

    //option 9
    protected void deleteEnquiry() {
        try {
            List<Enquiry> enquiryList = ApplicantController.getEnquiries();
            
            if (enquiryList == null || enquiryList.isEmpty()) {
                System.out.println("You don't have any enquiries to delete.");
                return;
            }
            
            System.out.println("Your Enquiries:");
            int cnt = 0;
            for (Enquiry e: enquiryList) {
                if (e != null) {
                    cnt += 1;
                    System.out.print(cnt + ". ");
                    e.viewEnquiry(currentUser.getUserRole());
                }
            }
            System.out.println();

            int enquiryIndex = getIntInput("Select the enquiry to delete (0 to cancel): ") - 1;
            
            if (enquiryIndex == -1) {
                System.out.println("Delete operation cancelled.");
                return;
            }
            
            if (enquiryIndex < 0 || enquiryIndex >= enquiryList.size()) {
                System.out.println("Invalid enquiry selection.");
                return;
            }
            
            Enquiry enquiry = enquiryList.get(enquiryIndex);

            ApplicantController.deleteEnquiry(enquiry);
        } catch (Exception e) {
            System.out.println("Error deleting enquiry: " + e.getMessage());
        }
    }

    
    //Helper method to display a list of projects
    private void displayProjectList(List<Project> projectList) {
        if (projectList == null || projectList.isEmpty()) {
            System.out.println("No projects to display.");
            return;
        }
        
        int cnt = 1;
        for (Project p : projectList) {
            if (p != null) {
                System.out.print(cnt + ". ");
                System.out.println(p.getName());
                cnt += 1;
            }
        }
    }

}
