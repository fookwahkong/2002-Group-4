package entity.user;

import entity.project.Project;
import enums.MaritalStatus;
import enums.UserRole;

public class HDBManager extends User {

    private Project currentProject;

    public HDBManager(String userID, String password, String name, int age, MaritalStatus maritalStatus,
            UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    //do we need this
    public void setCurrentProject(Project project) {
        this.currentProject = project;
    }

    public Project getCurrentProject() {
        return this.currentProject;
    }


    public String getId() {
        return super.getUserID();
    }

}
