package main.entity.user;

import main.enums.MaritalStatus;
import main.enums.UserRole;

public class Applicant extends User {

    public Applicant( String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }
    
    void viewProjects() {
        
    }
}
