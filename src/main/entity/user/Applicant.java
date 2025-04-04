package main.entity.user;

import main.entity.Enquiry;
import main.enums.MaritalStatus;
import main.enums.UserRole;
import main.entity.project.Project;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
    private List<Enquiry> enquiryList = new ArrayList<>();
    private List<Project> projectList = new ArrayList<>();

    public Applicant(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    public List<Enquiry> getEnquiryList() {
        return enquiryList;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public String getName() {
        return super.getName();
    }

    public void addEnquiry(Enquiry e) {
        enquiryList.add(e);
    }

    public void deleteEnquiry(Enquiry e) {
        enquiryList.remove(e);
    }

    void viewProjects() {

    }
}
