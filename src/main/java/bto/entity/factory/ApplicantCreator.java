package bto.entity.factory;

import bto.entity.user.Applicant;
import bto.enums.MaritalStatus;
import bto.enums.UserRole;

/**
 * A factory class for creating Applicant objects.
 */
public class ApplicantCreator implements UserCreator {
    
    @Override
    public Applicant createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        return new Applicant(userID, password, name, age, maritalStatus, UserRole.APPLICANT);
    }
}
