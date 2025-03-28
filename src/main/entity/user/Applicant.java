package main.entity.user;

import main.entity.project.Enquiry;
import main.enums.MaritalStatus;
import main.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
    private List<Enquiry> enquiryList = new ArrayList<>();

    public Applicant( String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    public List<Enquiry> getEnquiryList() {
        return enquiryList;
    }

    public void addEnquiry(Enquiry e) {
        enquiryList.add(e);
    }
    
    void viewProjects() {
        
    }
}
