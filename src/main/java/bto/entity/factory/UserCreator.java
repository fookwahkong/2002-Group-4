package bto.entity.factory;

import bto.entity.user.User;
import bto.enums.MaritalStatus;

public interface UserCreator {
    User createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus);
}
