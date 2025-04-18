package bto.entity.booking;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import bto.entity.project.Project;
import bto.entity.project.ProjectBuilder;
import bto.entity.user.BookingManager;
import bto.entity.user.HDBManager;
import bto.enums.MaritalStatus;

import java.time.LocalDate;
import java.util.Map;

class BookingManagerTest {

    private BookingManager bookingManager;
    private Project project1;
    private Project project2;
    
    @BeforeEach
    void setUp() {
        bookingManager = new BookingManager();
        
        HDBManager manager = new HDBManager("M001", "password", "Manager", 40,
                                    MaritalStatus.MARRIED);
        
        // Create test projects
        project1 = new ProjectBuilder()
                .withName("Project 1")
                .withNeighborhood("Area 1")
                .withVisibility(true)
                .withApplicationPeriod(
                    LocalDate.now().minusDays(10).toString(),
                    LocalDate.now().plusDays(20).toString()
                )
                .withManager(manager)
                .withOfficerSlots(3)
                .addHousingType("2-Room", 200000.0f, 50)
                .build();
                
        project2 = new ProjectBuilder()
                .withName("Project 2")
                .withNeighborhood("Area 2")
                .withVisibility(true)
                .withApplicationPeriod(
                    LocalDate.now().minusDays(5).toString(),
                    LocalDate.now().plusDays(15).toString()
                )
                .withManager(manager)
                .withOfficerSlots(2)
                .addHousingType("3-Room", 300000.0f, 30)
                .build();
    }
    
    @Test
    void testAddBooking() {
        bookingManager.setBookingDetails(project1, "2-Room");
        
        Map<Project, String> bookings = bookingManager.getBookingDetails();
        assertEquals(1, bookings.size());
        assertTrue(bookings.containsKey(project1));
        assertEquals("2-Room", bookings.get(project1));
    }
    
    @Test
    void testAddMultipleBookings() {
        bookingManager.setBookingDetails(project1, "2-Room");
        bookingManager.setBookingDetails(project2, "3-Room");
        
        Map<Project, String> bookings = bookingManager.getBookingDetails();
        assertEquals(2, bookings.size());
        assertEquals("2-Room", bookings.get(project1));
        assertEquals("3-Room", bookings.get(project2));
    }
    
    @Test
    void testUpdateBooking() {
        bookingManager.setBookingDetails(project1, "2-Room");
        bookingManager.setBookingDetails(project1, "3-Room"); // Update
        
        Map<Project, String> bookings = bookingManager.getBookingDetails();
        assertEquals(1, bookings.size());
        assertEquals("3-Room", bookings.get(project1));
    }
    
    @Test
    void testRemoveBooking() {
        bookingManager.setBookingDetails(project1, "2-Room");
        bookingManager.setBookingDetails(project2, "3-Room");
        
        bookingManager.removeBooking(project1);
        
        Map<Project, String> bookings = bookingManager.getBookingDetails();
        assertEquals(1, bookings.size());
        assertFalse(bookings.containsKey(project1));
        assertTrue(bookings.containsKey(project2));
    }
    
    @Test
    void testRemoveNonExistentBooking() {
        bookingManager.setBookingDetails(project2, "3-Room");
        
        // Should not throw exception
        bookingManager.removeBooking(project1);
        
        Map<Project, String> bookings = bookingManager.getBookingDetails();
        assertEquals(1, bookings.size());
        assertTrue(bookings.containsKey(project2));
    }
}