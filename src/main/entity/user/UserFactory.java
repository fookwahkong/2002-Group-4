package main.entity.user;

import main.enums.MaritalStatus;
import main.enums.UserRole;

public class UserFactory {
    public static User createUser(String userID, String password, int age, MaritalStatus maritalStatus, UserRole userRole) {
        return switch(userRole) {
            case APPLICANT -> new Applicant(userID, password, age, maritalStatus, userRole);
            case HDB_OFFICER -> new HDBOfficer(userID, password, age, maritalStatus, userRole);
            case HDB_MANAGER -> new HDBManager(userID, password, age, maritalStatus, userRole);
        };
    }
}
