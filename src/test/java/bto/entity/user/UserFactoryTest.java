package bto.entity.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import bto.entity.factory.UserCreator;

import bto.enums.MaritalStatus;
import bto.enums.UserRole;

class UserFactoryTest {

        @Test
        void testCreateUser() {
                UserFactory factory = new UserFactory();

                // Test creating an Applicant
                User applicant = factory.createUser("A001", "password", "John", 30,
                                MaritalStatus.SINGLE, UserRole.APPLICANT);
                assertNotNull(applicant);
                assertTrue(applicant instanceof Applicant);
                assertEquals("A001", applicant.getUserID());
                assertEquals(UserRole.APPLICANT, applicant.getUserRole());

                // Test creating an HDBOfficer
                User officer = factory.createUser("O001", "password", "Jane", 35,
                                MaritalStatus.MARRIED, UserRole.HDB_OFFICER);
                assertNotNull(officer);
                assertTrue(officer instanceof HDBOfficer);
                assertEquals("O001", officer.getUserID());
                assertEquals(UserRole.HDB_OFFICER, officer.getUserRole());

                // Test creating an HDBManager
                User manager = factory.createUser("M001", "password", "Bob", 40,
                                MaritalStatus.MARRIED, UserRole.HDB_MANAGER);
                assertNotNull(manager);
                assertTrue(manager instanceof HDBManager);
                assertEquals("M001", manager.getUserID());
                assertEquals(UserRole.HDB_MANAGER, manager.getUserRole());
        }

        @Test
        void testRegisterCustomCreator() {
                UserFactory factory = new UserFactory();

                // Create a mock creator for testing
                UserCreator mockCreator = (userId, password, name, age, maritalStatus) -> new Applicant(userId,
                                password,
                                "Custom" + name, age, maritalStatus, UserRole.APPLICANT);

                // Register the custom creator
                factory.registerCreator(UserRole.APPLICANT, mockCreator);

                // Create user with the custom creator
                User customUser = factory.createUser("C001", "password", "John", 25,
                                MaritalStatus.SINGLE, UserRole.APPLICANT);

                assertNotNull(customUser);
                assertEquals("CustomJohn", customUser.getName()); // Should use our custom logic
        }
}