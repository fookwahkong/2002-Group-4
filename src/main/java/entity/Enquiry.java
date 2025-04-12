package entity;

import entity.project.Project;
import entity.user.Applicant;
import enums.UserRole;


public class Enquiry {
    Applicant applicant;
    String message;
    String reply = "-";
    Project project;
    UserRole[] peopleValidforViewing = {UserRole.APPLICANT, UserRole.HDB_MANAGER};
    private boolean replied = false;


    public Enquiry(Applicant applicant, Project project, String message) {
        this.applicant = applicant;
        this.message = message;
        this.project = project;
    }

    public void viewEnquiry(UserRole role) {
        System.out.println("ENQUIRY");
        System.out.println("=======================================");
        if (role == UserRole.APPLICANT) {
            System.out.println("Enquiry on project " + project.getName() + ": " + message);
            System.out.println("Response: " + reply);
            System.out.println();
        } else {
            System.out.println("Enquiry from " + applicant.getName() + " on project " + project.getName() + ": " + message);
            System.out.println("Response: " + reply);
            System.out.println();
        }
    }

    public String getContent() {
        return this.message;
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

    public void setResponse(String response) {
        this.reply = response;
    }

    public String getReply() {
        return this.reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public boolean isReplied() {
        return this.replied;
    }

    public void setReplied(boolean status) {
        this.replied = status;
    }

}
