package entity.user;


import enums.MaritalStatus;
import enums.UserRole;

public class UserFactory {
    public static User createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        return switch (userRole) {
            case APPLICANT -> new Applicant(userID, password, name, age, maritalStatus, userRole);
            case HDB_OFFICER -> new HDBOfficer(userID, password, name, age, maritalStatus, userRole);
            case HDB_MANAGER -> new HDBManager(userID, password, name, age, maritalStatus, userRole);
        };
    }
        
    
}
