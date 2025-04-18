package bto.entity.user;

import bto.enums.MaritalStatus;
import bto.enums.UserRole;

public class HDBManager extends User {

    public HDBManager(String userID, String password, String name, int age, MaritalStatus maritalStatus,
            UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    public String getId() {
        return super.getUserID();
    }

}
