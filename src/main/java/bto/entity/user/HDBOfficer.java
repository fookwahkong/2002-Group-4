package bto.entity.user;

import bto.enums.MaritalStatus;
import bto.enums.UserRole;

/**
 * A class representing an HDB officer.
 */
public class HDBOfficer extends Applicant {

    private final UserRole userRole = UserRole.HDB_OFFICER;
    /**
     * Constructor for HDBOfficer
     * @param userID The user ID
     * @param password The password
     * @param name The name
     * @param age The age 
     * @param maritalStatus The marital status
     */
    public HDBOfficer(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        super(userID, password, name, age, maritalStatus);
    }

    @Override
    public UserRole getUserRole() {
        return this.userRole;
    }
}
