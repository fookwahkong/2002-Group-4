package main.enums;

public enum ProjectStatus {
    PENDING(false, "Project application is still pending."),
    SUCCESSFUL(true, "Project is application is successful."),
    UNSUCCESSFUL(false, "Project application is unsuccessful"),
    BOOKED(true, "Project has been applied and flat has been booked.");
    
    private final boolean visible;
    private final String description;
    
    ProjectStatus(boolean visible, String description) {
        this.visible = visible;
        this.description = description;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public String getDescription() {
        return description;
    }
}
