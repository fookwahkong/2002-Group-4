package bto.entity.user;

import bto.enums.MaritalStatus;
import bto.enums.UserRole;

/**
 * A class representing an HDB manager.
 */
public class HDBManager extends User {

    private final UserRole userRole = UserRole.HDB_MANAGER;
    
    /**
     * Constructor for HDBManager
     * @param userID The user ID
     * @param password The password
     * @param name The name
     * @param age The age
     * @param maritalStatus The marital status
     */
    public HDBManager(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        super(userID, password, name, age, maritalStatus);
    }

    /**
     * Gets the ID of the HDB manager
     * @return The ID of the HDB manager
     */

    @Override
    public UserRole getUserRole() {
        return this.userRole;
    }

}
