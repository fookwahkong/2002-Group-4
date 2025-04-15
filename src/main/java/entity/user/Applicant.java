package entity.user;

import java.util.HashMap;
import java.util.Map;

import entity.project.Project;
import enums.MaritalStatus;
import enums.UserRole;
import interfaces.BookingCapable;

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
