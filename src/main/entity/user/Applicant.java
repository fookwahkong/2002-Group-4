package main.entity.user;

import java.util.ArrayList;
import java.util.List;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.enums.MaritalStatus;
import main.enums.UserRole;

public class Applicant extends User {

    public Applicant(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    public String getName() {
        return super.getName();
    }

    public int getAge() {
        return super.getAge();
    }

    // public List<Project> viewOpenProjects(List<Project> allProjects) {
    //     List<Project> openProjects = new ArrayList<>();
    //     for (Project project : allProjects) {
    //         if (project.getVisibility()) {
    //             openProjects.add(project);
    //         }
    //     }
    //     return openProjects;

    // }

    // public List<Project> viewAppliedProjects() {
    //     return new ArrayList<>(projectList);
    // }

    // public boolean applyForProject(Project project) {
    //     if (project.getVisibility() && !projectList.contains(project)) {
    //         projectList.add(project);
    //         return true;
    //     }
    //     return false;
    // }

    // private boolean flatBooked = false;

    // public boolean bookFlat() {
    //     if (!flatBooked) {
    //         flatBooked = true;
    //         return true;
    //     }
    //     return false;
    // }

    // public boolean withdrawFromProject(Project project) {
    //     return projectList.remove(project);
    // }

    // public boolean withdrawFromFlat() {
    //     if (flatBooked) {
    //         flatBooked = false;
    //         return true;
    //     }
    //     return false;
    // }

}
