package bto.entity.factory;

import bto.entity.user.User;
import bto.enums.MaritalStatus;

/**
 * A factory interface for creating User objects.
 */
public interface UserCreator {

    /**
     * Creates a User object
     * @param userID The user ID
     * @param password The password
     * @param name The name
     * @param age The age
     * @param maritalStatus The marital status
     * @return The created User object
     */
    User createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus);
}
