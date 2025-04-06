package main.enums;

public enum ProjectStatus {
    DRAFT(false, "Project is in draft mode and not visible to applicants"),
    PUBLISHED(true, "Project is published and visible to applicants"),
    CLOSED(false, "Project application period has closed");
    
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
