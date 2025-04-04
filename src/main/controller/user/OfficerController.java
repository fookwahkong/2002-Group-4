package main.controller.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import main.entity.Registration;
import main.entity.project.Project;
import main.entity.user.HDBOfficer;

public class OfficerController {
    public static void submitRegistration(Project project) {
        HDBOfficer officer = (HDBOfficer) (UserManager.getInstance().getCurrentUser());

        // check if clash
        List<Project> assignProjects = officer.getAssignedProjects();
        List<Project> appliedProjects = officer.getProjectList();
        LocalDate projectStartDate = project.getOpeningDate();
        LocalDate projectEndDate = project.getClosingDate();

        Boolean canRegister = true;
        if (!appliedProjects.contains(project)) {
            for (Project p: assignProjects) {
                canRegister = (projectStartDate.isAfter(p.getClosingDate()) || projectEndDate.isBefore(p.getOpeningDate()) && (p.getRemainingSlots() > 0));
                if (!canRegister) {
                    break;
                }
            }
        } else {
            canRegister = false;
        }

        // Register
        if (canRegister) {
            Registration registration = new Registration(officer, project);
            project.addRegistration(registration);
            officer.addRegistration(registration);
            System.out.println("Registration Submitted.");
        } else {
            System.out.println("Registration Failed due to clash in Opening Date.");
        }
    }
}
