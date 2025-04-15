package controller.user;

import java.util.ArrayList;
import java.util.List;

import controller.project.ProjectController;
import controller.registration.RegistrationController;
import entity.Housing;
import entity.Registration;
import entity.project.Project;
import entity.user.Applicant;
import entity.user.HDBOfficer;
import enums.ProjectStatus;
import enums.RegistrationStatus;

public class OfficerController {

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

    public static void submitRegistration(Project project) {

        HDBOfficer officer = (HDBOfficer) (UserManager.getInstance().getCurrentUser());
        Registration registration = new Registration(officer, project, RegistrationStatus.PENDING);

        project.addRegistration(registration);
        RegistrationController.save();
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
