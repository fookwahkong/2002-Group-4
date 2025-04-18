package bto.controller.user;

import java.util.ArrayList;
import java.util.List;

import bto.controller.project.ProjectController;
import bto.controller.registration.RegistrationController;
import bto.entity.Housing;
import bto.entity.Registration;
import bto.entity.project.Project;
import bto.entity.user.Applicant;
import bto.entity.user.HDBOfficer;
import bto.enums.ProjectStatus;
import bto.enums.RegistrationStatus;

/**
 * A controller class for officer operations.
 */
public class OfficerController {

    /**
     * Updates the booking status of an applicant in a project.
     * 
     * @param project the project
     * @param applicant the applicant
     * @param projStatus the new project status
     */
    public static void updateBookingStatus(Project project, Applicant applicant, ProjectStatus projStatus){

        //if the program is robust enough, there should only be two types of applicants here (single (>35) or married (>21))
        String housingType;
        Housing house;
        if (ApplicantController.checkSingle(applicant) == true) {
            housingType = "2-Room";
            house = project.getHousingType(housingType);
        } else {
            housingType = "3-Room";
            house = project.getHousingType(housingType);
        }

        ProjectController.updateApplicantStatus(project, applicant, projStatus);
        ProjectController.updateHousingType(project, housingType , house.getSellingPrice(), house.getNumberOfUnits() - 1);
        ApplicantController.updateProfile(project,housingType);
        ProjectController.save();
    }

    /**
     * Submits a registration for a project.
     * 
     * @param project the project
     */
    public static void submitRegistration(Project project) {

        HDBOfficer officer = (HDBOfficer) (UserManager.getInstance().getCurrentUser());
        Registration registration = new Registration(officer, project, RegistrationStatus.PENDING);

        project.addRegistration(registration);
        RegistrationController.save();
    }

    /**
     * Retrieves the list of registrations for the current officer.
     * 
     * @return a list of Registration objects for the current officer
     */ 
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
