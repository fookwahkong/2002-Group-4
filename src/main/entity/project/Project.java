package main.entity.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.entity.Enquiry;
import main.entity.Housing;
import main.entity.Registration;
import main.entity.user.Applicant;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.enums.ProjectStatus;

public class Project {

    private String name;
    private boolean visible;
    private String neighborhood;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Map<String, Housing> housingTypes;
    private Map<Applicant, ProjectStatus> applicants; //applicants who applied for the project
    private HDBManager manager;
    private List<HDBOfficer> officers;
    private int officerSlot = 10;
    private List<Enquiry> enquiries;
    private List<Registration> registrations;

    public Project(String name, String neighborhood, boolean visible) {
        this.name = name;
        this.neighborhood = neighborhood;
        this.visible = visible;
        this.registrations = new ArrayList<>();
        this.housingTypes = new HashMap<>();
        this.applicants = new HashMap<>();
        this.officers = new ArrayList<>();
        this.enquiries = new ArrayList<>();
        this.registrations = new ArrayList<>();
    }

    public void addEnquiry(Enquiry enquiry) {
        this.enquiries.add(enquiry);
    }

    public void deleteEnquiry(Enquiry enquiry) {
        this.enquiries.remove(enquiry);
    }

    // Getter methods
    public String getName() {
        return this.name;
    }

    public boolean getVisibility() {
        return this.visible;
    }

    public String getNeighbourhood() {
        return this.neighborhood;
    }

    public LocalDate getOpeningDate() {
        return this.openingDate;
    }

    public LocalDate getClosingDate() {
        return this.closingDate;
    }

    public int getSlots() {
        return this.officerSlot;
    }

    public int getRemainingSlots() {
        return (this.officerSlot - officers.size());
    }

    public HDBManager getManager() {
        return this.manager;
    }

    public List<HDBOfficer> getAssignedOfficers() {
        return this.officers;
    }

    public List<Enquiry> getEnquiries() {
        return this.enquiries;
    }

    public List<Registration> getRegistrationList() {
        return registrations;
    }

    public Map<String, Housing> getAllHousingTypes() {
        return this.housingTypes;
    }

    public Housing getHousingType(String typeName) {
        return this.housingTypes.get(typeName);
    }

    public ProjectStatus getApplicantStatus(Applicant applicant) {
        return this.applicants.get(applicant);
    }

    public List<Applicant> getApplicants() {
        return new ArrayList<>(this.applicants.keySet());
    }

    public Map<Applicant, ProjectStatus> getApplicantswithStatus() {
        return this.applicants;
    }

    // Helper method to check if project is open for application
    public boolean isOpenForApplication() {
        LocalDate today = LocalDate.now();
        return today.isAfter(openingDate) && today.isBefore(closingDate);
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setVisiblity(boolean visible) {
        this.visible = visible;
    }

    public void setNeighbourhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public void setHousingType(String typeName, float sellingPrice, int numberOfUnits) {
        Housing housing = new Housing(typeName);
        housing.setSellingPrice(sellingPrice);
        housing.setNumberOfUnits(numberOfUnits);
        housingTypes.put(typeName, housing);
    }

    public void setManagerInCharge(HDBManager manager) {
        this.manager = manager;
    }

    public void setOfficerSlot(int slot) {
        if (this.officers.size() > slot) {
            throw new IllegalArgumentException("Cannot reduce slots below the number of assigned officers");
        }
        this.officerSlot = slot;
    }

    public void addOfficersIncharge(HDBOfficer officer) {
        if (this.officerSlot <= this.officers.size()) {
            throw new IllegalStateException("No more officer slots available");
        }
        this.officers.add(officer);
    }

    public void addRegistration(Registration registration) {
        this.registrations.add(registration);
    }

    public void addApplicant(Applicant applicant, ProjectStatus projStatus) {
        this.applicants.put(applicant, projStatus);
    }

    public void addOfficer(HDBOfficer officer) {
        this.officers.add(officer);
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", manager=" + manager +
                ", openingDate=" + openingDate +
                ", closingDate=" + closingDate +
                ", housingTypes=" + housingTypes.size() +
                '}';
    }
}