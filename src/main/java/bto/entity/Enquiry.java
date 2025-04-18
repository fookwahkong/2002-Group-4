package bto.entity;

import bto.entity.project.Project;
import bto.entity.user.Applicant;
import bto.enums.UserRole;

/**
 * A class representing an enquiry.
 */
public class Enquiry {
    Applicant applicant;
    String message;
    String reply = "-";
    Project project;
    private boolean replied = false;

    /**
     * Constructor for Enquiry
     * @param applicant The applicant
     * @param project The project
     * @param message The message
     */
    public Enquiry(Applicant applicant, Project project, String message) {
        this.applicant = applicant;
        this.message = message;
        this.project = project;
    }

    /**
     * Views the enquiry
     * @param role role of the user
     */
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

    /**
     * Gets the content of the enquiry
     * @return the content of the enquiry
     */
    public String getContent() {
        return this.message;
    }

    /**
     * Gets the project of the enquiry
     * @return the project of the enquiry
     */
    public Project getProject() {
        return this.project;
    }

    /**
     * Gets the applicant of the enquiry
     * @return the applicant of the enquiry
     */
    public Applicant getApplicant() {
        return this.applicant;
    }

    /**
     * Sets the message of the enquiry
     * @param newMessage the new message
     */
    public void setMessage(String newMessage) {
        this.message = newMessage;
    }

    /**
     * Gets the reply of the enquiry
     * @return the reply of the enquiry
     */
    public String getReply() {
        return this.reply;
    }

    /**
     * Sets the reply of the enquiry
     * @param reply the reply
     */
    public void setReply(String reply) {
        this.reply = reply;
    }

    /**
     * Checks if the enquiry is replied
     * @return true if the enquiry is replied, false otherwise
     */
    public boolean isReplied() {
        return this.replied;
    }

    /**
     * Sets the replied status of the enquiry
     * @param status the status
     */
    public void setReplied(boolean status) {
        this.replied = status;
    }

}
