package main.entity.project;

import main.entity.user.Applicant;

import java.util.ArrayList;
import java.util.List;

public class Enquiry {
    Applicant applicant;
    String message;
    String reply = "-";
    Project project;
    String[] peopleValidforViewing = {"applicant", "manager"};

    public Enquiry(Applicant applicant, Project project, String message) {
        this.applicant = applicant;
        this.message = message;
        this.project = project;
    }

    public void viewEnquiry(String viewer) {
        if (viewer.equals("applicant")) {
            System.out.println("Enquiry on project " + project.getName() + ": " + message);
            System.out.println("Response: " + reply);
            System.out.println();
        } else {
            System.out.println("Enquiry from " + applicant.getUserID() + "on project " + project.getName() + ": " + message);
            System.out.println("Response: " + reply);
            System.out.println();
        }
    }
}
