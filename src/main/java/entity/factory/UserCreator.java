package entity.factory;

import entity.user.User;
import enums.MaritalStatus;

public interface UserCreator {
    User createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus);
}
