package controller.registration;

import controller.project.ProjectController;
import entity.Registration;
import entity.project.Project;
import entity.user.HDBOfficer;
import utils.FileIOUtil;

import java.util.List;

public class RegistrationController {
    public static void load() {
        FileIOUtil.loadRegistration(ProjectController.getProjectList());
    }

    // get registration by project
    public static List<Registration> getRegistrationList(Project project) {
        return project.getRegistrationList();
    }

    public static void approveRegistration(Registration registration) {
        registration.approveRegistration();
        Project project = registration.getProject();
        HDBOfficer officer = registration.getOfficer();
        project.addOfficersIncharge(officer);
        System.out.println("Registration Approved.");
    }

    public static void rejectRegistration(Registration registration) {
        registration.rejectRegistration();
        System.out.println("Registration Rejected.");
    }
}
