package bto.controller.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bto.entity.user.User;
import bto.entity.user.Applicant;
import bto.entity.user.HDBOfficer;
import bto.entity.user.HDBManager;
import bto.enums.MaritalStatus;
import bto.enums.UserRole;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

class UserManagerTest {
    
    private UserManager userManager;
    
    @BeforeEach
    void setUp() throws Exception {
        // Get the singleton instance of UserManager
        userManager = UserManager.getInstance();
        
        // We need to set up the static users list using reflection
        // since there's no direct method to add users for testing
        List<User> testUsers = new ArrayList<>();
        
        // Create test users
        Applicant applicant = new Applicant("S1234567A", "password1", "John Doe", 30,
                MaritalStatus.SINGLE, UserRole.APPLICANT);
        HDBOfficer officer = new HDBOfficer("S2345678B", "password2", "Jane Smith", 35,
                MaritalStatus.MARRIED, UserRole.HDB_OFFICER);
        HDBManager manager = new HDBManager("S3456789C", "password3", "Bob Johnson", 40,
                MaritalStatus.MARRIED, UserRole.HDB_MANAGER);
                
        // Add to our test list
        testUsers.add(applicant);
        testUsers.add(officer);
        testUsers.add(manager);
        
        // Use reflection to set the private static users field
        Field usersField = UserManager.class.getDeclaredField("users");
        usersField.setAccessible(true);
        usersField.set(null, testUsers);
        
        // Reset currentUser for each test
        Field currentUserField = UserManager.class.getDeclaredField("currentUser");
        currentUserField.setAccessible(true);
        currentUserField.set(userManager, null);
    }
    
    @Test
    void testSuccessfulLogin() {
        // Test valid login for each user type
        User loggedInApplicant = userManager.login("S1234567A", "password1");
        assertNotNull(loggedInApplicant, "Applicant login should be successful");
        assertEquals("S1234567A", loggedInApplicant.getUserID());
        assertEquals(UserRole.APPLICANT, loggedInApplicant.getUserRole());
        assertEquals(loggedInApplicant, userManager.getCurrentUser());
        
        // Logout before testing next user
        userManager.logout();
        
        User loggedInOfficer = userManager.login("S2345678B", "password2");
        assertNotNull(loggedInOfficer, "Officer login should be successful");
        assertEquals("S2345678B", loggedInOfficer.getUserID());
        assertEquals(UserRole.HDB_OFFICER, loggedInOfficer.getUserRole());
        assertEquals(loggedInOfficer, userManager.getCurrentUser());
        
        userManager.logout();
        
        User loggedInManager = userManager.login("S3456789C", "password3");
        assertNotNull(loggedInManager, "Manager login should be successful");
        assertEquals("S3456789C", loggedInManager.getUserID());
        assertEquals(UserRole.HDB_MANAGER, loggedInManager.getUserRole());
        assertEquals(loggedInManager, userManager.getCurrentUser());
    }
    
    @Test
    void testFailedLogin() {
        // Test invalid userID
        User invalidUserLogin = userManager.login("S9999999X", "password1");
        assertNull(invalidUserLogin, "Login with invalid userID should fail");
        assertNull(userManager.getCurrentUser(), "No user should be set as current after failed login");
        
        // Test invalid password
        User invalidPasswordLogin = userManager.login("S1234567A", "wrongpassword");
        assertNull(invalidPasswordLogin, "Login with invalid password should fail");
        assertNull(userManager.getCurrentUser(), "No user should be set as current after failed login");
    }
    
    @Test
    void testLogout() {
        // First login
        User loggedInUser = userManager.login("S1234567A", "password1");
        assertNotNull(loggedInUser, "Login should be successful");
        assertEquals(loggedInUser, userManager.getCurrentUser());
        
        // Then logout
        userManager.logout();
        
        // Verify logout was successful
        assertNull(userManager.getCurrentUser(), "No user should be logged in after logout");
    }
    
    @Test
    void testVerifyNRIC() {
        // Test valid NRIC formats
        assertTrue(UserManager.verifyNRIC("S1234567A"), "S1234567A should be a valid NRIC format");
        assertTrue(UserManager.verifyNRIC("T9876543Z"), "T9876543Z should be a valid NRIC format");
        
        // Test invalid NRIC formats
        assertFalse(UserManager.verifyNRIC(""), "Empty string should be invalid NRIC format");
        assertFalse(UserManager.verifyNRIC("A123"), "A123 should be invalid NRIC format");
        assertFalse(UserManager.verifyNRIC("S123456"), "S123456 should be invalid NRIC format (too short)");
        assertFalse(UserManager.verifyNRIC("S12345678"), "S12345678 should be invalid NRIC format (too long)");
        assertFalse(UserManager.verifyNRIC("A1234567B"), "A1234567B should be invalid NRIC format (wrong first letter)");
        assertFalse(UserManager.verifyNRIC("S1234567a"), "S1234567a should be invalid NRIC format (lowercase last letter)");
    }
    
    @Test
    void testFindUserByID() {
        User foundUser = userManager.findUserByID("S1234567A");
        assertNotNull(foundUser, "Should find user with ID S1234567A");
        assertEquals("John Doe", foundUser.getName());
        
        User notFoundUser = userManager.findUserByID("nonexistent");
        assertNull(notFoundUser, "Should not find user with non-existent ID");
    }
    
    @Test
    void testFindUserByName() {
        User foundUser = userManager.findUserByName("Jane Smith");
        assertNotNull(foundUser, "Should find user with name Jane Smith");
        assertEquals("S2345678B", foundUser.getUserID());
        
        User notFoundUser = userManager.findUserByName("nonexistent");
        assertNull(notFoundUser, "Should not find user with non-existent name");
    }
}