package bto.entity.factory;


import bto.entity.user.HDBManager;
import bto.enums.MaritalStatus;
import bto.enums.UserRole;

/**
 * A factory class for creating HDBManager objects.
 */
public class HDBManagerCreator implements UserCreator {

    @Override
    public HDBManager createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        return new HDBManager(userID, password, name, age, maritalStatus, UserRole.HDB_MANAGER);
    }
} 