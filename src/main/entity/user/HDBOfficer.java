package main.entity.user;

import main.enums.MaritalStatus;
import main.enums.UserRole;

public class HDBOfficer extends Applicant {

    public HDBOfficer(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    public static List<Enquiry> getEnquiriesByOfficer(HDBOfficer officer) {
        List<Enquiry> officerEnquiries = new ArrayList<>();
        
        for (Project project : ProjectController.getOfficerProjects(officer)) {
            officerEnquiries.addAll(project.getEnquiriesHandledByOfficer(officer));
        }
        return officerEnquiries;
    }

    public static void replyToEnquiry(Enquiry enquiry, String response) {
        if (enquiry == null) {
            System.out.println("Invalid enquiry.");
            return;
        }
        enquiry.setResponse(response);
        enquiry.setReplied(true);  
        System.out.println("Response submitted: " + response);
    }
}
