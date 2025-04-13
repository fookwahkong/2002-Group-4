package entity.user;

import enums.MaritalStatus;
import enums.UserRole;

public class HDBManager extends User {

    public HDBManager(String userID, String password, String name, int age, MaritalStatus maritalStatus,
            UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    public String getId() {
        return super.getUserID();
    }

}
