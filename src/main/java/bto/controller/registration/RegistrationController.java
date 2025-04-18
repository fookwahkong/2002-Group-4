package bto.controller.registration;

import bto.controller.project.ProjectController;
import bto.entity.Registration;
import bto.entity.project.Project;
import bto.utils.FileIOUtil;

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

    public static boolean approveRegistration(Registration registration) {
        Project project = registration.getProject();
        if (project.getRemainingSlots() <= 0) {
            return false;
        }
        registration.approveRegistration();
        ProjectController.updateOfficer(registration.getProject(), registration.getOfficer());
        save();
        return true;
    }

    public static void rejectRegistration(Registration registration) {
        registration.rejectRegistration();
        save();
        System.out.println("Registration Rejected.");
    }
}
