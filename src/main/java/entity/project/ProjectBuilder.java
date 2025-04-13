package entity.project;

import entity.Housing;
import entity.user.Applicant;
import entity.user.HDBManager;
import utils.FileIOUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectBuilder {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String name;
    private String neighborhood;
    private boolean visible = true;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private HDBManager manager;
    private int officerSlots;
    private Map<String, Housing> housingTypes = new HashMap<>();

    public ProjectBuilder() {
        // Default constructor
    }

    public ProjectBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProjectBuilder withNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
        return this;
    }

    public ProjectBuilder withVisibility(boolean visible) {
        this.visible = visible;
        return this;
    }

    public ProjectBuilder withOpeningDate(String openingDate) {
        this.openingDate = parseDate(openingDate);
        return this;
    }

    public ProjectBuilder withOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
        return this;
    }

    public ProjectBuilder withClosingDate(String closingDate) {
        this.closingDate = parseDate(closingDate);
        return this;
    }

    public ProjectBuilder withClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
        return this;
    }

    public ProjectBuilder withApplicationPeriod(String openingDate, String closingDate) {
        this.openingDate = parseDate(openingDate);
        this.closingDate = parseDate(closingDate);
        return this;
    }

    public ProjectBuilder withApplicationPeriod(LocalDate openingDate, LocalDate closingDate) {
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        return this;
    }

    public ProjectBuilder withManager(HDBManager manager) {
        this.manager = manager;
        return this;
    }

    public ProjectBuilder withOfficerSlots(int officerSlots) {
        this.officerSlots = officerSlots;
        return this;
    }

    public ProjectBuilder addHousingType(String typeName, float price, int units) {
        Housing housing = new Housing(typeName);
        housing.setSellingPrice(price);
        housing.setNumberOfUnits(units);
        housingTypes.put(typeName, housing);
        return this;
    }

    public Project build() {
        validateProjectData();

        Project project = new Project(name, neighborhood, visible);

        // Set dates
        if (openingDate != null && closingDate != null) {
            project.setOpeningDate(openingDate);
            project.setClosingDate(closingDate);
        }
        
        // Set manager
        if (manager != null) {
            project.setManagerInCharge(manager);
        }

        // Set officer slots
        project.setOfficerSlot(officerSlots);

        // Set housing
        for (Map.Entry<String, Housing> entry : housingTypes.entrySet()) {
            Housing housing = entry.getValue();
            project.setHousingType(
                    housing.getTypeName(),
                    housing.getSellingPrice(),
                    housing.getNumberOfUnits());
        }

        
        return project;
    }

    private void validateProjectData() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("Project name cannot be empty");
        }

        if (neighborhood == null || neighborhood.trim().isEmpty()) {
            throw new IllegalStateException("Neighborhood cannot be empty");
        }

        if (openingDate != null && closingDate != null && openingDate.isAfter(closingDate)) {
            throw new IllegalStateException("Opening date cannot be after closing date");
        }

        if (officerSlots < 0) {
            throw new IllegalStateException("Officer slots cannot be negative");
        }
    }

    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString.trim(), formatter);
    }

    public void save(List<Project> projects) {
        FileIOUtil.saveProjectToFile(projects, FileIOUtil.PROJECTS_FILE);
    }
}