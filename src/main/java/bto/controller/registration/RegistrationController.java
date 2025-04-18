package bto.controller.registration;

import bto.controller.project.ProjectController;
import bto.entity.Registration;
import bto.entity.project.Project;
import bto.utils.FileIOUtil;

import java.util.List;

/**
 * Controller class for handling registration-related operations.
 */
public class RegistrationController {
    /**
     * Loads registration data for all projects using FileIOUtil.
     */
    public static void load() {
        FileIOUtil.loadRegistration(ProjectController.getProjectList());
    }

    /**
     * Saves all project registrations to file using FileIOUtil.
     */
    public static void save() {
        FileIOUtil.saveRegistrationsToFile(ProjectController.getProjectList());
    }

    /**
     * Retrieves the list of registrations for a given project.
     *
     * @param project the project whose registrations are to be retrieved
     * @return a list of Registration objects for the specified project
     */
    public static List<Registration> getRegistrationList(Project project) {
        return project.getRegistrationList();
    }

    /**
     * Approves a registration if the project has remaining slots, updates the officer, and saves the state.
     *
     * @param registration the registration to approve
     * @return true if the registration was approved, false if no slots are available
     */
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

    /**
     * Rejects a registration and saves the state.
     *
     * @param registration the registration to reject
     */
    public static void rejectRegistration(Registration registration) {
        registration.rejectRegistration();
        save();
        System.out.println("Registration Rejected.");
    }
}
