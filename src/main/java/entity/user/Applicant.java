package entity.user;

import java.util.HashMap;
import java.util.Map;

import entity.project.Project;
import enums.MaritalStatus;
import enums.UserRole;

public class Applicant extends User {

    private Map<Project, String> bookingDetails;

    public Applicant(String userID, String password, String name, int age, MaritalStatus maritalStatus,
            UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);

        this.bookingDetails = new HashMap<>();
    }

    public String getName() {
        return super.getName();
    }

    public int getAge() {
        return super.getAge();
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
