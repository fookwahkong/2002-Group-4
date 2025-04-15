package entity.factory;

import entity.user.Applicant;
import entity.user.User;
import enums.MaritalStatus;
import enums.UserRole;

public class ApplicantCreator implements UserCreator {
    
    @Override
    public User createUser(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        return new Applicant(userID, password, name, age, maritalStatus, UserRole.APPLICANT);
    }
}
