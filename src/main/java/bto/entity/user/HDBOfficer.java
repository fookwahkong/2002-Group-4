package bto.entity.user;

import bto.enums.MaritalStatus;
import bto.enums.UserRole;

/**
 * A class representing an HDB officer.
 */
public class HDBOfficer extends Applicant {

    /**
     * Constructor for HDBOfficer
     * @param userID The user ID
     * @param password The password
     * @param name The name
     * @param age The age 
     * @param maritalStatus The marital status
     * @param userRole The user role
     */
    public HDBOfficer(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

}
