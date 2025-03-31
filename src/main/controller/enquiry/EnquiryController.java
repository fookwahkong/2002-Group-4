package main.controller.enquiry;

import java.lang.management.PlatformManagedObject;
import java.util.*;

import main.controller.project.ProjectController;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.entity.user.User;
import main.enums.UserRole;
import main.utils.FileIOUtil;

public class EnquiryController {

    private static List<Enquiry> enquiries = new ArrayList<>();

    //get enquiries on ALL project
    public static List<Enquiry> getEnquiriesList(User user) {
        UserRole userRole = user.getUserRole();

        if (userRole == UserRole.HDB_MANAGER) {
            List<Project> projectList = ProjectController.getProjectList(user); //get ALL the projects
            
            for (Project p : projectList) {
                enquiries.addAll(p.getEnquiries());
            }
            return enquiries;

        } else {

            return enquiries;
        }
    } 

    public static List<Enquiry> getEnquiriesByManager(HDBManager manager) {
        List<Project> projectList = ProjectController.getManagerProjects(manager); //get the projects handled by Manager
            
        for (Project p : projectList) {
            enquiries.addAll(p.getEnquiries());
        }
        return enquiries;
    }

    public static List<Enquiry> getEnquiriesByOfficer(HDBOfficer officer) {
        List<Project> projectList = ProjectController.getOfficerProjects(officer); //get the projects handled by Officer
            
            for (Project p : projectList) {
                enquiries.addAll(p.getEnquiries());
            }
            return enquiries;
    }

    public static void replyToEnquiry(Enquiry selectedEnquiry, String reply) {
        selectedEnquiry.setReply(reply);
    }
}
