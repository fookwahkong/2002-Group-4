package main.entity.project;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.entity.Enquiry;
import main.entity.Housing;
import main.entity.Registration;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.enums.ProjectStatus;

public class Project {

    private String id; // New: Unique identifier
    private String name;
    private boolean visible;
    private String neighborhood;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private HDBManager manager;
    private List<HDBOfficer> officers;
    private List<HDBOfficer> pendingOfficers;
    private int officerSlot = 10;
    private List<Enquiry> enquiries;
    private List<Registration> registrations;
    private Map<String, Housing> housingTypes;

    public Project(String name, String neighborhood, boolean visible) {
        this.name = name;
        this.neighborhood = neighborhood;
        this.visible = visible;
        this.officers = new ArrayList<>();
        this.pendingOfficers = new ArrayList<>();
        this.enquiries = new ArrayList<>();
        this.registrations = new ArrayList<>();
        this.housingTypes = new HashMap<>();
    }

    public void setHousingType(String typeName, float sellingPrice, int numberOfUnits) {
        Housing housing = new Housing(typeName);
        housing.setSellingPrice(sellingPrice);
        housing.setNumberOfUnits(numberOfUnits);
        housingTypes.put(typeName, housing);
    }

    public void addOfficersIncharge(HDBOfficer officer) {
        if (this.officerSlot <= this.officers.size()) {
            throw new IllegalStateException("No more officer slots available");
        }
        this.officers.add(officer);
    }

    public void requestToJoin(HDBOfficer officer) {
        if (this.pendingOfficers == null) {
            this.pendingOfficers = new ArrayList<>();
        }
        this.pendingOfficers.add(officer);
    }

    public void approveOfficer(HDBOfficer officer) {
        if (this.officerSlot <= this.officers.size()) {
            throw new IllegalStateException("No more officer slots available");
        }
        if (this.pendingOfficers.contains(officer)) {
            this.pendingOfficers.remove(officer);
            this.officers.add(officer);
        } else {
            throw new IllegalArgumentException("Officer not in pending list");
        }
    }

    public void rejectOfficer(HDBOfficer officer) {
        if (this.pendingOfficers.contains(officer)) {
            this.pendingOfficers.remove(officer);
        } else {
            throw new IllegalArgumentException("Officer not in pending list");
        }
    }

    public void addEnquiry(Enquiry enquiry) {
        this.enquiries.add(enquiry);
    }

    public void deleteEnquiry(Enquiry enquiry) {
        this.enquiries.remove(enquiry);
    }

    public void addRegistration(Registration registration) {
        this.registrations.add(registration);
    }

    /**
     * Displays all details of a project in a formatted manner.
     * Can be used in a console or for generating reports.
     */
    public static String displayProjectDetails(Project project) {
        if (project == null) {
            return "Project not found.";
        }

        StringBuilder details = new StringBuilder();

        // Format dates nicely
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        String openingDate = project.getOpeningDate() != null ? project.getOpeningDate().format(displayFormat)
                : "Not set";
        String closingDate = project.getClosingDate() != null ? project.getClosingDate().format(displayFormat)
                : "Not set";

        // Format currency with commas and 2 decimal places
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        // Build the details string
        details.append("=".repeat(60)).append("\n");
        details.append(String.format("PROJECT: %s\n", project.getName().toUpperCase())).append("\n");
        details.append("=".repeat(60)).append("\n\n");

        // Basic information
        details.append("BASIC INFORMATION\n");
        details.append("-".repeat(30)).append("\n");
        details.append(String.format("Neighborhood: %s\n", project.getNeighbourhood()));
        details.append(String.format("Status: %s\n", project.getStatus()));
        details.append(String.format("Visibility: %s\n",
                project.getVisibility() ? "Visible to applicants" : "Hidden from applicants"));
        details.append(String.format("Application Period: %s to %s\n", openingDate, closingDate));
        details.append("\n");

        // Housing information
        details.append("HOUSING TYPES\n");
        details.append("-".repeat(30)).append("\n");

        Map<String, Housing> housingTypes = project.getAllHousingTypes();
        if (housingTypes != null && !housingTypes.isEmpty()) {
            // Display using the new housing types map
            for (Map.Entry<String, Housing> entry : housingTypes.entrySet()) {
                Housing housing = entry.getValue();
                details.append(String.format("Type: %s\n", entry.getKey()));
                details.append(String.format("  Price: %s\n", currencyFormat.format(housing.getSellingPrice())));
                details.append(String.format("  Available Units: %d\n", housing.getNumberOfUnits()));
                details.append("\n");
            }
        } 

        // Management information
        details.append("MANAGEMENT\n");
        details.append("-".repeat(30)).append("\n");
        details.append(String.format("Manager: %s\n",
                project.getManager() != null ? project.getManager().getName() : "Unassigned"));
        details.append(String.format("Officer Slots: %d/%d (Assigned/Total)\n",
                project.getAssignedOfficers().size(), project.getSlots()));

        // Display assigned officers
        List<HDBOfficer> officers = project.getAssignedOfficers();
        if (officers != null && !officers.isEmpty()) {
            details.append("Assigned Officers:\n");
            int count = 1;
            for (HDBOfficer officer : officers) {
                details.append(String.format("  %d. %s\n", count++, officer.getName()));
            }
        } else {
            details.append("No officers assigned to this project.\n");
        }
        details.append("\n");

        // Pending officer requests
        List<HDBOfficer> pendingOfficers = project.getPendingOfficers();
        if (pendingOfficers != null && !pendingOfficers.isEmpty()) {
            details.append("Pending Officer Requests:\n");
            int count = 1;
            for (HDBOfficer officer : pendingOfficers) {
                details.append(String.format("  %d. %s\n", count++, officer.getName()));
            }
            details.append("\n");
        }

        // Registration information
        List<Registration> registrations = project.getRegistrationList();
        details.append("REGISTRATIONS\n");
        details.append("-".repeat(30)).append("\n");
        if (registrations != null && !registrations.isEmpty()) {
            details.append(String.format("Total Registrations: %d\n", registrations.size()));
            // You could add more detailed registration statistics here
        } else {
            details.append("No registrations for this project.\n");
        }
        details.append("\n");

        // Enquiry information
        List<Enquiry> enquiries = project.getEnquiries();
        details.append("ENQUIRIES\n");
        details.append("-".repeat(30)).append("\n");
        if (enquiries != null && !enquiries.isEmpty()) {
            details.append(String.format("Total Enquiries: %d\n", enquiries.size()));
            // You could add more detailed enquiry statistics here
        } else {
            details.append("No enquiries for this project.\n");
        }

        details.append("\n");
        details.append("=".repeat(60)).append("\n");

        return details.toString();
    }

    // Getter methods
    public String getId() {
        return this.id;
    }

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

    public List<HDBOfficer> getPendingOfficers() {
        return this.pendingOfficers;
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

    // Helper method to check if project is open for application
    public boolean isOpenForApplication() {
        LocalDate today = LocalDate.now();
        return today.isAfter(openingDate) && today.isBefore(closingDate);
    }

    // Setter methods
    public void setId(String id) {
        this.id = id;
    }

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

    public void setDate(LocalDate openingDate, LocalDate closingDate) {
        this.openingDate = openingDate;
        this.closingDate = closingDate;
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

    // Utility method to get project status
    public ProjectStatus getStatus() {
        if (!visible) {
            return ProjectStatus.DRAFT;
        }

        LocalDate today = LocalDate.now();
        if (closingDate != null && today.isAfter(closingDate)) {
            return ProjectStatus.CLOSED;
        }

        return ProjectStatus.PUBLISHED;
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