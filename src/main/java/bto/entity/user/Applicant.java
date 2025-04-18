package bto.entity.user;


import java.util.Map;

import bto.entity.project.Project;
import bto.enums.MaritalStatus;
import bto.enums.UserRole;
import bto.interfaces.BookingCapable;

/**
 * A class representing an applicant.
 */
public class Applicant extends User implements BookingCapable{

    private final BookingManager bookingManager;
    private final UserRole userRole = UserRole.APPLICANT;

    public Applicant(String userID, String password, String name, int age, MaritalStatus maritalStatus) {
        super(userID, password, name, age, maritalStatus);

        this.bookingManager = new BookingManager();
    }

    @Override
    public UserRole getUserRole() {
        return this.userRole;
    }

    @Override
    public void setBookingDetails(Project project, String housingType) {
        this.bookingManager.setBookingDetails(project, housingType);
    }

    @Override
    public Map<Project, String> getBookingDetails() {
        return this.bookingManager.getBookingDetails();
    }

    @Override
    public void removeBooking(Project project) {
        this.bookingManager.removeBooking(project);
    }
}
