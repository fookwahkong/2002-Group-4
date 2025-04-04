package main.controller.registration;

import main.entity.Registration;
import main.controller.project.ProjectController;
import main.entity.project.Project;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;

import java.util.ArrayList;
import java.util.List;

public class RegistrationController {
    public static void load() {}

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
