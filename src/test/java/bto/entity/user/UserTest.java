package bto.entity.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import bto.enums.MaritalStatus;
import bto.enums.UserRole;

class UserTest {

    private Applicant applicant;
    private HDBOfficer officer;
    private HDBManager manager;

    @BeforeEach
    void setUp() {
        applicant = new Applicant("A001", "password", "John Doe", 30,
                MaritalStatus.SINGLE);
        officer = new HDBOfficer("O001", "password", "Jane Smith", 35,
                MaritalStatus.MARRIED);
        manager = new HDBManager("M001", "password", "Bob Johnson", 40,
                MaritalStatus.MARRIED);
    }

    @Test
    void testUserAuthentication() {
        // Test Authenticatable interface methods
        assertEquals("A001", applicant.getUserID());
        assertEquals("password", applicant.getPassword());

        applicant.changePassword("newPassword");
        assertEquals("newPassword", applicant.getPassword());
    }

    @Test
    void testPersonalProfile() {
        // Test PersonalProfile interface methods
        assertEquals("John Doe", applicant.getName());
        assertEquals(30, applicant.getAge());
        assertEquals(MaritalStatus.SINGLE, applicant.getMaritalStatus());

        assertEquals("Jane Smith", officer.getName());
        assertEquals(35, officer.getAge());
        assertEquals(MaritalStatus.MARRIED, officer.getMaritalStatus());
    }

    @Test
    void testUserRole() {
        assertEquals(UserRole.APPLICANT, applicant.getUserRole());
        assertEquals(UserRole.HDB_OFFICER, officer.getUserRole());
        assertEquals(UserRole.HDB_MANAGER, manager.getUserRole());
    }
}