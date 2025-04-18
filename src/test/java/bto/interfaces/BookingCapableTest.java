package bto.interfaces;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import bto.entity.project.Project;
import bto.entity.project.ProjectBuilder;
import bto.entity.user.Applicant;
import bto.entity.user.HDBManager;
import bto.enums.MaritalStatus;
import java.time.LocalDate;
import java.util.Map;

class BookingCapableTest {
    
    private BookingCapable bookingCapable;
    private Project project;
    
    @BeforeEach
    void setUp() {
        // Create an Applicant (which implements BookingCapable)
        bookingCapable = new Applicant("A001", "password", "John Doe", 30, 
                                MaritalStatus.SINGLE);
        
        // Create a test project
        HDBManager manager = new HDBManager("M001", "password", "Manager", 40,
                                    MaritalStatus.MARRIED);
        
        project = new ProjectBuilder()
                .withName("Test Project")
                .withNeighborhood("Test Area")
                .withVisibility(true)
                .withApplicationPeriod(
                    LocalDate.now().minusDays(10).toString(),
                    LocalDate.now().plusDays(20).toString()
                )
                .withManager(manager)
                .withOfficerSlots(3)
                .addHousingType("2-Room", 200000.0f, 50)
                .build();
    }
    
    @Test
    void testBookingOperations() {
        // Initially no bookings
        Map<Project, String> bookings = bookingCapable.getBookingDetails();
        assertTrue(bookings.isEmpty());
        
        // Add booking
        bookingCapable.setBookingDetails(project, "2-Room");
        bookings = bookingCapable.getBookingDetails();
        
        // Verify booking was added
        assertEquals(1, bookings.size());
        assertTrue(bookings.containsKey(project));
        assertEquals("2-Room", bookings.get(project));
        
        // Remove booking
        bookingCapable.removeBooking(project);
        bookings = bookingCapable.getBookingDetails();
        assertTrue(bookings.isEmpty());
    }
}