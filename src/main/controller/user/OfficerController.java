package main.controller.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import main.controller.project.ProjectController;
import main.entity.Housing;
import main.entity.Registration;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.entity.user.HDBOfficer;
import main.enums.ProjectStatus;
import main.enums.RegistrationStatus;
import main.utils.FileIOUtil;

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
