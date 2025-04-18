package bto.entity.user;

import java.util.HashMap;
import java.util.Map;

import bto.entity.project.Project;

public class BookingManager {
    private final Map<Project, String> bookingDetails;

    public BookingManager() {
        this.bookingDetails = new HashMap<>();
    }
    
    public void setBookingDetails(Project project, String housingType) {
        this.bookingDetails.put(project, housingType);
    }

    public Map<Project, String> getBookingDetails() {
        return this.bookingDetails;
    }

    public void removeBooking(Project project) {
        this.bookingDetails.remove(project);
    }
}
