package bto.interfaces;

import java.util.Map;

import bto.entity.project.Project;

/**
 * Interface for objects capable of handling project bookings.
 */
public interface BookingCapable {
    /**
     * Sets the booking details for a project and housing type.
     *
     * @param project the project to book
     * @param housingType the type of housing to book
     */
    void setBookingDetails(Project project, String housingType);
    /**
     * Gets the booking details as a map of projects to housing types.
     *
     * @return a map containing project and housing type pairs
     */
    Map<Project, String> getBookingDetails();
    /**
     * Removes the booking for the specified project.
     *
     * @param project the project whose booking is to be removed
     */
    void removeBooking(Project project);
}
