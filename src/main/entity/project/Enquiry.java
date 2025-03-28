package main.entity.project;

import main.entity.user.Applicant;

import java.util.ArrayList;
import java.util.List;

public class Enquiry {
    Applicant applicant;
    String message;
    String reply;
    String[] peopleValidforViewing = {"applicant", "manager"};

    public Enquiry(Applicant applicant, String message) {
        this.applicant = applicant;
        this.message = message;
    }

    void viewEnquiry(String viewer) {
        if (viewer.equals("applicant") || viewer.equals("manager")) {
            System.out.println("Enquiry from " + applicant.getUserID() + ": " + message); //TEMPORARILY USE userID
        } else {
            System.out.println("You do not have permission to view this enquiry.");
        }
    }


}
