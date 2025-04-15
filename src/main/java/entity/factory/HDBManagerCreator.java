package entity.factory;


import entity.user.HDBManager;
import entity.user.User;
import enums.MaritalStatus;
import enums.UserRole;

public class HDBManagerCreator implements UserCreator {

    @Override
    public User createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        return new HDBManager(userID, password, name, age, maritalStatus, UserRole.HDB_MANAGER);
    }
} 