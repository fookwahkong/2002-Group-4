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

    public Project getProject() {
        return this.project;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public void setMessage(String newMessage) {
        this.message = newMessage;
    }

    private boolean replied = false;

    public void setResponse(String response) {
        this.reply = response;
    }


    public String getReply() {
        return this.reply;
    }

    public void setReplied(boolean status) {
        this.replied = status;
    }

    public boolean isReplied() {
        return this.replied;
    }

}
