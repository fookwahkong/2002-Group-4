package bto.interfaces;

import java.util.Map;

import bto.entity.project.Project;

public interface BookingCapable {
    void setBookingDetails(Project project, String housingType);
    Map<Project, String> getBookingDetails();
    void removeBooking(Project project);
}
