package bto.controller.enquiry;

import bto.controller.project.ProjectController;
import bto.entity.Enquiry;
import bto.entity.project.Project;
import bto.entity.user.HDBOfficer;
import bto.entity.user.User;
import bto.enums.UserRole;
import bto.utils.FileIOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A controller class for enquiry operations.
 */
public class EnquiryController {

    /**
     * Load the enquiries.
     */
    public static void load() {
        FileIOUtil.loadEnquiries(ProjectController.getProjectList());
    }

    /**
     * Save the enquiries.
     */
    public static void save(){
        FileIOUtil.saveEnquiriesToFile(ProjectController.getProjectList());
    }

    /**
     * Get the enquiries on all projects.
     * 
     * @param user the user
     * @return the enquiries
     */
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

    /**
     * Get the enquiries by officer.
     * 
     * @param officer the officer
     * @return the enquiries
     */
    public static List<Enquiry> getEnquiriesByOfficer(HDBOfficer officer) {
        List<Enquiry> result = new ArrayList<>(); // Create a new list each time
        List<Project> projectList = ProjectController.getOfficerProjects(officer);

        for (Project p : projectList) {
            result.addAll(p.getEnquiries());
        }

        return result;
    }

    /**
     * Reply to an enquiry.
     * 
     * @param selectedEnquiry the selected enquiry
     * @param reply the reply
     */
    public static void replyToEnquiry(Enquiry selectedEnquiry, String reply) {
        selectedEnquiry.setReply(reply);
        selectedEnquiry.setReplied(true);
        save();
    }
}
