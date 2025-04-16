package utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import entity.user.*;
import enums.MaritalStatus;
import enums.UserRole;
import interfaces.BookingCapable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class FileIOUtilTest {

    @TempDir
    Path tempDir;

    private String testUsersFile;
    private List<User> testUsers;

    @BeforeEach
    void setUp() throws IOException {
        // Create test directory
        testUsersFile = tempDir.resolve("test_users.csv").toString();

        // Create test users
        testUsers = new ArrayList<>();
        UserFactory factory = new UserFactory();

        testUsers.add(factory.createUser("A001", "pass1", "Test User 1", 25,
                MaritalStatus.SINGLE, UserRole.APPLICANT));
        testUsers.add(factory.createUser("O001", "pass2", "Test Officer", 35,
                MaritalStatus.MARRIED, UserRole.HDB_OFFICER));
        testUsers.add(factory.createUser("M001", "pass3", "Test Manager", 45,
                MaritalStatus.MARRIED, UserRole.HDB_MANAGER));

        // Create test CSV file
        try (FileWriter writer = new FileWriter(testUsersFile)) {
            writer.write("Name,UserID,Age,MaritalStatus,Password\n");
            writer.write("Test User 1,A001,25,Single,pass1\n");
            writer.write("Test Officer,O001,35,Married,pass2\n");
            writer.write("Test Manager,M001,45,Married,pass3\n");
        }
    }

    @Test
    void testLoadUsersFromFile() {
        List<User> loadedUsers = FileIOUtil.loadUsersFromFile(testUsersFile, UserRole.APPLICANT);

        assertEquals(3, loadedUsers.size());

        User user1 = loadedUsers.get(0);
        assertEquals("Test User 1", user1.getName());
        assertEquals("A001", user1.getUserID());
        assertEquals(25, user1.getAge());
        assertEquals(MaritalStatus.SINGLE, user1.getMaritalStatus());
        assertEquals("pass1", user1.getPassword());

        // Since we're forcing UserRole.APPLICANT for all loaded users
        assertEquals(UserRole.APPLICANT, user1.getUserRole());
    }

    @Test
    void testSaveUsersToFile() {
        String outputFile = tempDir.resolve("output_users.csv").toString();

        FileIOUtil.saveUsersToFile(testUsers, outputFile);

        // Check if file was created
        File file = new File(outputFile);
        assertTrue(file.exists());

        // Load the saved users and verify
        List<User> loadedUsers = new ArrayList<>();

        // We need to load each type separately since our test file doesn't distinguish
        // roles
        loadedUsers.addAll(FileIOUtil.loadUsersFromFile(outputFile, UserRole.APPLICANT));

        assertEquals(3, loadedUsers.size());

        // Verify a sample user
        boolean foundUser = false;
        for (User user : loadedUsers) {
            if (user.getUserID().equals("A001")) {
                assertEquals("Test User 1", user.getName());
                assertEquals(25, user.getAge());
                assertEquals(MaritalStatus.SINGLE, user.getMaritalStatus());
                foundUser = true;
                break;
            }
        }
        assertTrue(foundUser, "Test User 1 should be found in saved file");
    }

    @Test
    void testBookingCapableOperations() {
        // Create a booking-capable user
        Applicant applicant = new Applicant("A002", "pass", "Booking User", 30,
                MaritalStatus.SINGLE, UserRole.APPLICANT);

        // Verify it implements BookingCapable
        assertTrue(applicant instanceof BookingCapable);

        // Get the booking interface
        BookingCapable bookingCapable = (BookingCapable) applicant;

        // Test that initial bookings are empty
        assertTrue(bookingCapable.getBookingDetails().isEmpty());
    }
}
