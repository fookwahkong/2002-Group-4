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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
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

}
