package main.entity.project;

import main.entity.user.Applicant;

import java.util.ArrayList;
import java.util.List;

public class Enquiry {
    public static List<Enquiry> enquiryList = new ArrayList<>();

    Applicant applicant;
    Project project;
    String message;
    String reply;
    String[] peopleValidforViewing = {"applicant", "manager"};

    public Enquiry(Applicant applicant, Project project, String message) {
        this.applicant = applicant;
        this.project = project;
        this.message = message;
    }

    public static void addEnquiry(Enquiry enquiry) {
        enquiryList.add(enquiry);
        // System.out.println(enquiryList);
    }

    void viewEnquiry(String viewer) {
        if (viewer.equals("applicant") || viewer.equals("manager")) {
            System.out.println("Enquiry from " + applicant.getUserID() + ": " + message); //TEMPORARILY USE userID
        } else {
            System.out.println("You do not have permission to view this enquiry.");
        }
    }


}
