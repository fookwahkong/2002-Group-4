package entity.factory;


import entity.user.HDBManager;
import enums.MaritalStatus;
import enums.UserRole;

public class HDBManagerCreator implements UserCreator {

    @Override
    public HDBManager createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        return new HDBManager(userID, password, name, age, maritalStatus, UserRole.HDB_MANAGER);
    }
} 