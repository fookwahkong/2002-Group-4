package entity.user;

import enums.MaritalStatus;
import enums.UserRole;



public class HDBOfficer extends Applicant {

    public HDBOfficer(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

}
