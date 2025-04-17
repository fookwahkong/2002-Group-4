package controller.registration;

import controller.project.ProjectController;
import entity.Registration;
import entity.project.Project;
import utils.FileIOUtil;

import java.util.List;

public class RegistrationController {
    public static void load() {
        FileIOUtil.loadRegistration(ProjectController.getProjectList());
    }

    public static void save() {
        FileIOUtil.saveRegistrationsToFile(ProjectController.getProjectList());
    }

    // get registration by project
    public static List<Registration> getRegistrationList(Project project) {
        return project.getRegistrationList();
    }

    public static void approveRegistration(Registration registration) {
        registration.approveRegistration();
        ProjectController.updateOfficer(registration.getProject(), registration.getOfficer());
        save();
        System.out.println("Registration Approved.");
    }

    public static void rejectRegistration(Registration registration) {
        registration.rejectRegistration();
        save();
        System.out.println("Registration Rejected.");
    }
}
