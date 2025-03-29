package main.controller.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import main.entity.project.Project;
import main.entity.user.HDBManager;
import main.entity.user.User;
import main.utils.FileIOUtil;


public class ProjectController
{
  private static List<Project> projects = new ArrayList<>();

  public ProjectController()
  {
  }

  public static void load()
  {
    projects = FileIOUtil.loadProjects();
  }

  public static List<Project> getProjectList(User user)
  {
    return projects;
  }

  public static List<Project> getManagerProjects(HDBManager manager)
  {
    return ProjectController.getProjectList(manager).stream()
        .filter(project -> project.getManager().equals(manager))
        .toList();
  }

  public static List<Project> getOfficerProjects(HDBOfficer officer) 
  {
    return ProjectController.getProjectList(officer).stream()
        .filter(project -> project.getAssignedOfficers().contains(officer))
        .toList();
  }


  public static void createProject(String projectName, String neighbourhood, float priceOne,
                                   int numberOfUnitsOne, float priceTwo, int numberOfUnitsTwo,
                                   String openingDate, String closingDate, HDBManager manager,
                                   int officerSlots)
  {
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
  }

  public static void deleteProject(Project project) {
    projects.remove(project);
  }

}
