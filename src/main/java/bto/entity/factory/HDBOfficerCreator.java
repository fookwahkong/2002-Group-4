package bto.entity.factory;

import bto.entity.user.HDBOfficer;
import bto.enums.MaritalStatus;
import bto.enums.UserRole;

/**
 * A factory class for creating HDBOfficer objects.
 */
public class HDBOfficerCreator implements UserCreator {

    @Override
    public HDBOfficer createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        return new HDBOfficer(userID, password, name, age, maritalStatus, UserRole.HDB_OFFICER);
    }
}