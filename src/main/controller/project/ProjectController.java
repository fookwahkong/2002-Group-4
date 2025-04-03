package main.controller.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import main.entity.project.Project;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.entity.user.Applicant;
import main.utils.FileIOUtil;

public class ProjectController {
  private static List<Project> projects = new ArrayList<>();
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

  public ProjectController() {
  }

  public static void load() {
    projects = FileIOUtil.loadProjects();
  }

  //get ALL the projects
  public static List<Project> getProjectList() {
    return projects;
  }

  //get projects managed by manager
  public static List<Project> getManagerProjects(HDBManager user) {
    return ProjectController.getProjectList().stream()
        .filter(project -> project.getManager().equals(user))
        .toList();
  }

  //get projects managed by officer
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


    Project project = new Project(
        projectName,
        neighbourhood,
        true // Assuming this is always true for now
    );

    // Set housing type one details
    project.setHousingTypeOne(
        priceOne, // Selling price
        numberOfUnitsOne);

    // Set housing type two details
    project.setHousingTypeTwo(
        priceTwo, // Selling price
        numberOfUnitsTwo);

    // Set application dates
    project.setDate(
        LocalDate.parse(openingDate.trim(), formatter),
        LocalDate.parse(closingDate.trim(), formatter));

    project.setManagerInCharge(manager);

    // Set officer slot
    project.setOfficerSlot(officerSlots);

    projects.add(project);
    
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

  public static void updateProjectPriceTypeOne(Project project, float price) {
    project.setPriceTypeOne(price);
  }

  public static void updateProjectNoOfUnitsTypeOne(Project project, int units) {
    project.setNoOfUnitsTypeOne(units);
  }

  public static void updateProjectPriceTypeTwo(Project project, float price) {
    project.setPriceTypeTwo(price);
  }

  public static void updateProjectNoOfUnitsTypeTwo(Project project, int units) {
    project.setNoOfUnitsTypeTwo(units);
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

}
