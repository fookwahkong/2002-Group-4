package entity.factory;

import entity.user.HDBOfficer;
import enums.MaritalStatus;
import enums.UserRole;

public class HDBOfficerCreator implements UserCreator {

    @Override
    public HDBOfficer createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        return new HDBOfficer(userID, password, name, age, maritalStatus, UserRole.HDB_OFFICER);
    }
}