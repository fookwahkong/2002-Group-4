package entity.user;

import java.util.HashMap;
import java.util.Map;

import entity.factory.ApplicantCreator;
import entity.factory.HDBManagerCreator;
import entity.factory.HDBOfficerCreator;
import entity.factory.UserCreator;
import enums.MaritalStatus;
import enums.UserRole;

public class UserFactory {
    private final Map<UserRole, UserCreator> creators = new HashMap<>();
    
    public UserFactory() {
        // Register default creators
        creators.put(UserRole.APPLICANT, new ApplicantCreator());
        creators.put(UserRole.HDB_OFFICER, new HDBOfficerCreator());
        creators.put(UserRole.HDB_MANAGER, new HDBManagerCreator());
    }
    
    public User createUser(String userID, String password, String name, int age, 
                        MaritalStatus maritalStatus, UserRole userRole) {
        UserCreator creator = creators.get(userRole);
        if (creator == null) {
            throw new IllegalArgumentException("Invalid user role: " + userRole);
        }
        return creator.createUser(userID, password, name, age, maritalStatus);
    }
    
    // Register new user types without modifying the factory
    public void registerCreator(UserRole role, UserCreator creator) {
        creators.put(role, creator);
    }
}