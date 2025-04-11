package main.entity;

import main.entity.project.Project;
import main.entity.user.HDBOfficer;
import main.enums.RegistrationStatus;

public class Registration {
    HDBOfficer officer;
    Project project;
    RegistrationStatus status;

    public Registration(HDBOfficer officer, Project project, RegistrationStatus status) {
        this.officer = officer;
        this.project = project;
        this.status = status;
    }

    public void viewRegistration() {
        System.out.println("REGISTRATION");
        System.out.println("=======================================");
        System.out.println("Name of Applicant: " + this.officer.getName());
        System.out.println("Project: " + this.project.getName());
        System.out.println("Status: " + this.status);
    }

    public void approveRegistration() {
        this.status = RegistrationStatus.APPROVED;
    }

    public void rejectRegistration() {
        this.status = RegistrationStatus.REJECTED;
    }

    public Project getProject() {
        return this.project;
    }
    
    public HDBOfficer getOfficer() {
        return this.officer;
    }

    public RegistrationStatus getRegistrationStatus() {
        return status;
    }
}
