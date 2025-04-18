package bto.entity.user;

import bto.enums.MaritalStatus;
import bto.enums.UserRole;

/**
 * A class representing an HDB manager.
 */
public class HDBManager extends User {

    /**
     * Constructor for HDBManager
     * @param userID The user ID
     * @param password The password
     * @param name The name
     * @param age The age
     * @param maritalStatus The marital status
     * @param userRole The user role
     */
    public HDBManager(String userID, String password, String name, int age, MaritalStatus maritalStatus,
            UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    /**
     * Gets the ID of the HDB manager
     * @return The ID of the HDB manager
     */
    public String getId() {
        return super.getUserID();
    }

}
