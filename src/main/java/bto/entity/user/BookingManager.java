package bto.entity.user;

import java.util.HashMap;
import java.util.Map;

import bto.entity.project.Project;

/**
 * A class representing a booking manager.
 */
public class BookingManager {
    private final Map<Project, String> bookingDetails;

    /**
     * Constructor for BookingManager
     */
    public BookingManager() {
        this.bookingDetails = new HashMap<>();
    }
    
    /**
     * Sets the booking details
     * @param project The project
     * @param housingType The housing type
     */
    public void setBookingDetails(Project project, String housingType) {
        this.bookingDetails.put(project, housingType);
    }

    /**
     * Gets the booking details
     * @return The booking details
     */
    public Map<Project, String> getBookingDetails() {
        return this.bookingDetails;
    }

    /**
     * Removes a booking
     * @param project The project
     */
    public void removeBooking(Project project) {
        this.bookingDetails.remove(project);
    }
}
