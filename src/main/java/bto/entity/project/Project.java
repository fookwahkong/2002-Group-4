package bto.entity.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bto.entity.Enquiry;
import bto.entity.Housing;
import bto.entity.Registration;
import bto.entity.user.Applicant;
import bto.entity.user.HDBManager;
import bto.entity.user.HDBOfficer;
import bto.enums.ProjectStatus;

/**
 * A class representing a project.
 */
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
    private int officerSlot;
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

    /**
     * Adds an enquiry to the project
     * @param enquiry The enquiry to add
     */
    public void addEnquiry(Enquiry enquiry) {
        this.enquiries.add(enquiry);
    }

    /**
     * Deletes an enquiry from the project
     * @param enquiry The enquiry to delete
     */
    public void deleteEnquiry(Enquiry enquiry) {
        this.enquiries.remove(enquiry);
    }

    /**
     * Gets the name of the project
     * @return The name of the project
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the visibility of the project
     * @return The visibility of the project
     */ 
    public boolean getVisibility() {
        return this.visible;
    }

    /**
     * Gets the neighbourhood of the project
     * @return The neighbourhood of the project
     */ 
    public String getNeighbourhood() {
        return this.neighborhood;
    }

    /**
     * Gets the opening date of the project
     * @return The opening date of the project
     */     
    public LocalDate getOpeningDate() {
        return this.openingDate;
    }

    /**
     * Gets the closing date of the project
     * @return The closing date of the project
     */ 
    public LocalDate getClosingDate() {
        return this.closingDate;
    }

    /**
     * Gets the number of slots for the project
     * @return The number of slots for the project
     */     
    public int getSlots() {
        return this.officerSlot;
    }

    /**
     * Gets the remaining slots for the project
     * @return The remaining slots for the project
     */      
    public int getRemainingSlots() {
        return (this.officerSlot - officers.size());
    }

    /**
     * Gets the manager of the project
     * @return The manager of the project
     */ 
    public HDBManager getManager() {
        return this.manager;
    }

    /**
     * Gets the assigned officers of the project
     * @return The assigned officers of the project
     */     
    public List<HDBOfficer> getAssignedOfficers() {
        return this.officers;
    }

    /**
     * Gets the enquiries of the project
     * @return The enquiries of the project
     */         
    public List<Enquiry> getEnquiries() {
        return this.enquiries;
    }

    /**
     * Gets the registration list of the project
     * @return The registration list of the project
     */         
    public List<Registration> getRegistrationList() {
        return registrations;
    }

    /**
     * Gets all the housing types of the project
     * @return The housing types of the project
     */             
    public Map<String, Housing> getAllHousingTypes() {
        return this.housingTypes;
    }

    /**
     * Gets the housing type of the project
     * @param typeName The type name of the housing
     * @return The housing type of the project
     */                 
    public Housing getHousingType(String typeName) {
        return this.housingTypes.get(typeName);
    }

    /**
     * Gets the applicant status of the project
     * @param applicant The applicant
     * @return The applicant status of the project
     */             
    public ProjectStatus getApplicantStatus(Applicant applicant) {
        return this.applicants.get(applicant);
    }

    /**
     * Gets the applicants of the project
     * @return The applicants of the project
     */              
    public List<Applicant> getApplicants() {
        return new ArrayList<>(this.applicants.keySet());
    }

    /**
     * Gets the applicants with status of the project
     * @return The applicants with status of the project
     */              
    public Map<Applicant, ProjectStatus> getApplicantswithStatus() {
        return this.applicants;
    }

    /**
     * Checks if the project is open for application
     * @return True if the project is open for application, false otherwise
     */              
    public boolean isOpenForApplication() {
        LocalDate today = LocalDate.now();
        return today.isAfter(openingDate) && today.isBefore(closingDate);
    }

    /**
     * Sets the name of the project
     * @param name The name of the project
     */              
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the visibility of the project
     * @param visible The visibility of the project
     */                   
    public void setVisiblity(boolean visible) {
        this.visible = visible;
    }

    /**
     * Sets the neighbourhood of the project
     * @param neighborhood The neighbourhood of the project
     */                     
    public void setNeighbourhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    /**
     * Sets the opening date of the project
     * @param openingDate The opening date of the project
     */                         
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    /**
     * Sets the closing date of the project
     * @param closingDate The closing date of the project
     */                             
    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    /**
     * Sets the housing type of the project
     * @param typeName The type name of the housing
     * @param sellingPrice The selling price of the housing
     * @param numberOfUnits The number of units of the housing
     */                     
    public void setHousingType(String typeName, float sellingPrice, int numberOfUnits) {
        Housing housing = new Housing(typeName);
        housing.setSellingPrice(sellingPrice);
        housing.setNumberOfUnits(numberOfUnits);
        housingTypes.put(typeName, housing);
    }

    /**
     * Sets the manager in charge of the project
     * @param manager The manager in charge of the project
     */                      
    public void setManagerInCharge(HDBManager manager) {
        this.manager = manager;
    }

    /**
     * Sets the number of officer slots for the project
     * @param slot The number of officer slots for the project
     */                      
    public void setOfficerSlot(int slot) {
        if (this.officers.size() > slot) {
            throw new IllegalArgumentException("Cannot reduce slots below the number of assigned officers");
        }
        this.officerSlot = slot;
    }

    /**
     * Adds an officer in charge of the project
     * @param officer The officer in charge of the project
     */                      
    public void addOfficersIncharge(HDBOfficer officer) {
        if (this.officerSlot <= this.officers.size()) {
            throw new IllegalStateException("No more officer slots available");
        }
        this.officers.add(officer);
    }

    /**
     * Adds a registration to the project
     * @param registration The registration to add
     */                       
    public void addRegistration(Registration registration) {
        this.registrations.add(registration);
    }

    /**
     * Adds an applicant to the project
     * @param applicant The applicant to add
     * @param projStatus The project status of the applicant
     */                         
    public void addApplicant(Applicant applicant, ProjectStatus projStatus) {
        this.applicants.put(applicant, projStatus);
    }

    /**
     * Adds an officer to the project
     * @param officer The officer to add
     */                             
    public void addOfficer(HDBOfficer officer) {
        this.officers.add(officer);
    }

    /**
     * Returns a string representation of the project
     * @return A string representation of the project
     */ 
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