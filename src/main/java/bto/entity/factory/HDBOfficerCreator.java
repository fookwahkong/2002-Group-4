package bto.entity.factory;

import bto.entity.user.HDBOfficer;
import bto.enums.MaritalStatus;

/**
 * A factory class for creating HDBOfficer objects.
 */
public class HDBOfficerCreator implements UserCreator {

    /**
     * Creates a HDBOfficer object
     * @param userID The user ID
     * @param password The password
     * @param name The name
     * @param age The age
     * @param maritalStatus The marital status
     * @return The created HDBOfficer object
     */
    @Override
    public HDBOfficer createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        return new HDBOfficer(userID, password, name, age, maritalStatus);
    }
}