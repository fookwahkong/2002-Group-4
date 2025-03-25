package main.entity.project;

import main.entity.user.User;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.entity.Housing;
import main.controller.Application;
import main.entity.project.Enquiry;
import main.controller.Registration;

public abstract class ProjectDetails {

    private String name;
    private boolean visible;
    private String neighborhood;
    private Date openingDate;
    private Date closingDate;
    private HDBManager manager;
    private List<HDBOfficer> officers;
    private List<Application> applications;
    private List<Enquiry> enquiries;
    private List<Registration> registrations;
    private Housing housingType;
    private int numberofHousing;

    public ProjectDetails(String name, String neighborhood, boolean visible) {
        this.name = name;
        this.neighborhood = neighborhood;
        this.visible = visible;
        this.officers = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
        this.registrations = new ArrayList<>();
    }

    public void setDate(Date openingDate, Date closingDate) {
        this.openingDate = openingDate;
        this.closingDate = closingDate;
    }

    public void setManagerInCharge(HDBManager manager) {
        this.manager = manager;
    }

    public void addOfficersIncharge(HDBOfficer officer) {
        this.officers.add(officer);
    }

    public void addApplication(Application application) {
        this.applications.add(application);
    }

    public void addEnquiry(Enquiry enquiry) {
        this.enquiries.add(enquiry);
    }

    public void addRegistration(Registration registration) {
        this.registrations.add(registration);
    }

    public void setHousingType(Housing housingType) {
        this.housingType = housingType;
    }

    public List<Registration> getRegistrationList(User user) {
        return registrations;
    }

    public List<Application> getApplicationList() {
        return applications;
    }
}
