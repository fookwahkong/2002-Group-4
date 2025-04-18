package bto.entity.user;

import bto.enums.MaritalStatus;
import bto.enums.UserRole;

/**
 * A class representing an HDB officer.
 */
public class HDBOfficer extends Applicant {

    public HDBOfficer(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

}
