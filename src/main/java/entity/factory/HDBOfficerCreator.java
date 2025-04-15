package entity.factory;

import entity.user.HDBOfficer;
import entity.user.User;
import enums.MaritalStatus;
import enums.UserRole;

public class HDBOfficerCreator implements UserCreator {

    @Override
    public User createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        return new HDBOfficer(userID, password, name, age, maritalStatus, UserRole.HDB_OFFICER);
    }
}