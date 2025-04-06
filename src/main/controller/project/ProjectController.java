package main.controller.project;

import main.entity.project.Project;
import main.entity.project.ProjectBuilder;
import main.entity.user.Applicant;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.utils.FileIOUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProjectController {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static List<Project> projects = new ArrayList<>();

    public ProjectController() {
    }

    public static void load() {
        projects = FileIOUtil.loadProjects();
    }

    // get ALL the projects
    public static List<Project> getProjectList() {
        return projects;
    }

    // get projects managed by manager
    public static List<Project> getManagerProjects(HDBManager user) {
        return ProjectController.getProjectList().stream()
                .filter(project -> {
                    System.out.println("Comparing managers: " + project.getManager() + " and " + user);
                    return project.getManager().equals(user);
                })
                .toList();
    }

    // get projects managed by officer
    public static List<Project> getOfficerProjects(HDBOfficer officer) {
        return ProjectController.getProjectList().stream()
                .filter(project -> project.getAssignedOfficers().contains(officer))
                .toList();
    }

    public static List<Project> getApplicantProjects(Applicant applicant) {
        return ProjectController.getProjectList().stream()
                .filter(project -> project.getVisibility() == true)
                .toList();
    }

    public static void createProject(String projectName, String neighbourhood, float priceOne,
            int numberOfUnitsOne, float priceTwo, int numberOfUnitsTwo,
            String openingDate, String closingDate, HDBManager manager,
            int officerSlots) {
            
            ProjectBuilder projectBuilder = new ProjectBuilder();
            Project project = projectBuilder
                .withName(projectName)
                .withNeighborhood(neighbourhood)
                .withVisibility(true)
                .addHousingType("2-Room", priceOne, numberOfUnitsOne)
                .addHousingType("3-Room", priceTwo, numberOfUnitsTwo)
                .withApplicationPeriod(openingDate, closingDate)
                .withManager(manager)
                .withOfficerSlots(officerSlots)
                .build();
    
        projects.add(project);
        FileIOUtil.saveProjectToFile(projects, FileIOUtil.PROJECTS_FILE);
    }

    public static void deleteProject(Project project) {
        projects.remove(project);
    }

    public static boolean isProjectVisible(Project project) {
        return project.getVisibility();
    }

    public static void toggleProjectVisibility(Project project) {
        project.setVisiblity(!project.getVisibility());
    }

    public static void updateProjectName(Project project, String name) {
        project.setName(name);
    }

    public static void updateProjectNeighbourhood(Project project, String neighbourhood) {
        project.setNeighbourhood(neighbourhood);
    }

    public static void updateProjectOpeningDate(Project project, String openingDate) {
        project.setOpeningDate(LocalDate.parse(openingDate.trim(), formatter));
    }

    public static void updateProjectClosingDate(Project project, String closingDate) {
        project.setClosingDate(LocalDate.parse(closingDate.trim(), formatter));
    }

    public static void updateProjectSlots(Project project, int slots) {
        project.setOfficerSlot(slots);
    }
    
    public static void updateHousingType(Project project, String typeName, float sellingPrice, int numberOfUnits) {
        project.setHousingType(typeName, sellingPrice, numberOfUnits);
    }
}
