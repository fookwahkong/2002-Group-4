package main.entity.user;

import main.enums.MaritalStatus;
import main.enums.UserRole;

public class HDBOfficer extends Applicant {

    public HDBOfficer(String userID, String password, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, age, maritalStatus, userRole);
    }
}
