package main.controller.enquiry;

import main.controller.project.ProjectController;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.entity.user.User;
import main.enums.UserRole;
import main.utils.FileIOUtil;

import java.util.ArrayList;
import java.util.List;

public class EnquiryController {

    public static void load() {
        FileIOUtil.loadEnquiries(ProjectController.getProjectList());
    }

    //get enquiries on ALL project
    public static List<Enquiry> getEnquiriesList(User user) {
        List<Enquiry> result = new ArrayList<>(); // Create a new list each time
        UserRole userRole = user.getUserRole();

        if (userRole == UserRole.HDB_MANAGER) {
            List<Project> projectList = ProjectController.getProjectList();
            for (Project p : projectList) {
                result.addAll(p.getEnquiries());
            }
        }
        return result;
    }

    public static List<Enquiry> getEnquiriesByManager(HDBManager manager) {
        List<Enquiry> result = new ArrayList<>(); // Create a new list each time
        List<Project> projectList = ProjectController.getManagerProjects(manager);

        for (Project p : projectList) {
            result.addAll(p.getEnquiries());
        }

        return result;
    }

    public static List<Enquiry> getEnquiriesByOfficer(HDBOfficer officer) {
        List<Enquiry> result = new ArrayList<>(); // Create a new list each time
        List<Project> projectList = ProjectController.getOfficerProjects(officer);

        for (Project p : projectList) {
            result.addAll(p.getEnquiries());
        }

        return result;
    }

    public static void replyToEnquiry(Enquiry selectedEnquiry, String reply) {
        selectedEnquiry.setReply(reply);
    }
}
