package main.controller.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import main.controller.project.ProjectController;
import main.entity.Registration;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.entity.user.HDBOfficer;

public class OfficerController {

    public static void submitRegistration(Project project) {
        HDBOfficer officer = (HDBOfficer) (UserManager.getInstance().getCurrentUser());

        // check if clash
        List<Project> assignProjects = ProjectController.getOfficerProjects(officer);
        List<Applicant> appliedApplicants = project.getApplicants();
        LocalDate projectStartDate = project.getOpeningDate();
        LocalDate projectEndDate = project.getClosingDate();

        Boolean canRegister = true;
        if (!appliedApplicants.contains(officer)) {
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
            System.out.println("Registration Submitted.");
        } else {
            System.out.println("Registration Failed due to clash in Opening Date.");
        }
    }

    public static List<Registration> getRegistrationList() {
        HDBOfficer officer = (HDBOfficer) (UserManager.getInstance().getCurrentUser());
        List<Registration> returnList = new ArrayList<>();

        List<Project> projectList = ProjectController.getProjectList();
        for (Project p: projectList) {
            List<Registration> registrationList = p.getRegistrationList();
            for (Registration r: registrationList) {
                if (r.getOfficer().equals(officer)) {
                    returnList.add(r);
                }
            }
        }
        return returnList;
    }

}
