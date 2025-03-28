package main.controller.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import main.controller.user.UserManager;
import main.entity.project.Project;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.utils.FileIOUtil;
import main.entity.user.User;


public class ProjectController {
    private static List<Project> projects = new ArrayList<>();
    public ProjectController() {
    }

    public static List<Project> getProjectList(User user) {
        return projects;
    } 

    public static List<Project> getProjectListFiltered(User user, int filter) {
        List<Project> projectlist = new ArrayList<>();
        switch(filter) {
            default: break;
        }
        return projectlist;
    }

    public static void load() {
        projects = FileIOUtil.loadProjects();
        for (Project p: projects) {
            System.out.println(p.getName());
        }
    }
    public Project findProject() {
        return null;
    };

    public static void createProject(String projectName, String neighbourhood, float priceOne, int numberOfUnitsOne, float priceTwo, int numberOfUnitsTwo, String openingDate, String closingDate, HDBManager manager, int officerSlots) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        Project project = new Project(
                projectName,
                neighbourhood,
                true  // Assuming this is always true for now
        );

        // Set housing type one details
        project.setHousingTypeOne(
                priceOne, // Selling price
                numberOfUnitsOne
        );

        // Set housing type two details
        project.setHousingTypeTwo(
                priceTwo, // Selling price
                numberOfUnitsTwo
        );

        // Set application dates
        project.setDate(
                LocalDate.parse(openingDate.trim(), formatter),
                LocalDate.parse(closingDate.trim(), formatter)
        );

        project.setManagerInCharge(manager);

        // Set officer slot
        project.setOfficerSlot(officerSlots);

        projects.add(project);

        for (Project p: projects) {
            System.out.println(p.getName());
        }
    }

}
