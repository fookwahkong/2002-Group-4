package bto.enums;

/**
 * Enum representing the status of a project.
 */
public enum ProjectStatus {
    PENDING(false, "Project application is still pending."),
    SUCCESSFUL(true, "Project is application is successful."),
    UNSUCCESSFUL(false, "Project application is unsuccessful"),
    BOOKED(true, "Project has been applied and flat has been booked."),
    REQUEST_BOOK(true, "Project application was successful and flat booking is pending."),
    REQUEST_WITHDRAW(true, "Project application is requested to be WITHDRAWN.");

    private final boolean visible;
    private final String description;
    
    /**
     * Constructor for ProjectStatus.
     * 
     * @param visible whether the project is visible to applicants
     * @param description the description of the project status
     */
    ProjectStatus(boolean visible, String description) {
        this.visible = visible;
        this.description = description;
    }
    
    /**
     * Returns whether the project is visible to applicants.
     * 
     * @return true if the project is visible, false otherwise
     */
    public boolean isVisible() {
        return visible;
    }
    
    /**
     * Returns the description of the project status.
     * 
     * @return the description of the project status
     */
    public String getDescription() {
        return description;
    }
}
