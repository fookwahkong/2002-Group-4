package bto.entity.user;


import java.util.Map;

import bto.entity.project.Project;
import bto.enums.MaritalStatus;
import bto.enums.UserRole;
import bto.interfaces.BookingCapable;

public class Applicant extends User implements BookingCapable{

    private final BookingManager bookingManager;

    public Applicant(String userID, String password, String name, int age, MaritalStatus maritalStatus,
            UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);

        this.bookingManager = new BookingManager();
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
