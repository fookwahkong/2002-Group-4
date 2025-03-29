package main.boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import main.controller.project.ProjectController;
import main.controller.user.UserManager;
import main.entity.project.Project;
import main.entity.user.HDBManager;

public class ManagerUI
{
  private static final Scanner scanner = new Scanner(System.in);
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
  private final HDBManager currentUser = (HDBManager) UserManager.getInstance().getCurrentUser();

  public void showMenu()
  {
    while (true)
    {
      displayMenuOptions();

      try
      {
        int choice = getValidIntInput(1, 4);

        switch (choice)
        {
          case 1 -> viewProjects();
          case 2 -> createHDBProject();
          case 3 -> editHDBProject();
          case 4 -> deleteHDBProject();
          case 0 ->
          {
            return; // Exit method
          }
        }
      } catch (Exception e)
      {
        System.out.println("An error occurred: " + e.getMessage());
      }
    }
  }

  private void displayMenuOptions()
  {
    System.out.println("\nMANAGER UI");
    System.out.println("==================================");
    System.out.println("1. View projects");
    System.out.println("2. Create HDB Project");
    System.out.println("3. Edit HDB Project");
    System.out.println("4. Delete HDB Project");
    System.out.println("0. Exit");
    System.out.print("Enter your choice: ");
  }

  private void viewProjects()
  {
    int filterChoice = getIntInput("Select projects to view (1. All 2. Self): ");
    switch (filterChoice)
    {
      case 1 ->
      {
        for (Project p : ProjectController.getProjectList(currentUser))
        {
          System.out.println(p.getName());
        }
      }
      case 2 ->
      {
        for (Project p : ProjectController.getManagerProjects(currentUser))
        {
          System.out.println(p.getName());
        }
      }
    }

  }

  private void createHDBProject()
  {
    try
    {
      String projectName = getStringInput("Enter project name: ");
      String neighbourHood = getStringInput("Enter neighbourhood: ");

      float priceOne = getFloatInput("Enter type 1 (2-Room) price: ");
      int noOfUnitsOne = getIntInput("Enter number of units for 2-Room: ");

      float priceTwo = getFloatInput("Enter type 2 (3-Room) price: ");
      int noOfUnitsTwo = getIntInput("Enter number of units for 3-Room: ");

      LocalDate openingDate = getDateInput("Enter opening date (mm/dd/yyyy): ");
      LocalDate closingDate = getDateInput("Enter closing date (mm/dd/yyyy): ");

      int slots = getIntInput("Enter number of officer slots: ");

      ProjectController.createProject(
          projectName,
          neighbourHood,
          priceOne,
          noOfUnitsOne,
          priceTwo,
          noOfUnitsTwo,
          openingDate.format(DATE_FORMATTER),
          closingDate.format(DATE_FORMATTER),
          (HDBManager) currentUser,
          slots
      );

      System.out.println("HDB Project created successfully!");
    } catch (Exception e)
    {
      System.out.println("Failed to create HDB Project: " + e.getMessage());
    }
  }

  private void editHDBProject()
  {
    System.out.println("Edit HDB Project functionality not implemented yet.");
    // TODO: Implement project editing logic
  }

  private void deleteHDBProject()
  {
    List<Project> projectList = ProjectController.getProjectList(currentUser);
    int cnt = 1;
    for (Project p : projectList)
    {
      System.out.print(cnt + ". ");
      System.out.println(p.getName());
      cnt += 1;
    }

    int projIndex = getIntInput("Select the project to submit enquiry for: ") - 1;
    Project proj = projectList.get(projIndex);
    ProjectController.deleteProject(proj);
  }

  private String getStringInput(String prompt)
  {
    System.out.print(prompt);
    return scanner.nextLine().trim();
  }

  private int getIntInput(String prompt)
  {
    while (true)
    {
      try
      {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e)
      {
        System.out.println("Invalid input. Please enter a valid integer.");
      }
    }
  }

  private float getFloatInput(String prompt)
  {
    while (true)
    {
      try
      {
        System.out.print(prompt);
        return Float.parseFloat(scanner.nextLine());
      } catch (NumberFormatException e)
      {
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
  }

  private LocalDate getDateInput(String prompt)
  {
    while (true)
    {
      try
      {
        System.out.print(prompt);
        return LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
      } catch (DateTimeParseException e)
      {
        System.out.println("Invalid date format. Please use mm/dd/yyyy.");
      }
    }
  }

  private int getValidIntInput(int min, int max)
  {
    while (true)
    {
      try
      {
        int input = Integer.parseInt(scanner.nextLine());
        if (input >= min && input <= max)
        {
          return input;
        }
        System.out.printf("Please enter a number between %d and %d%n", min, max);
      } catch (NumberFormatException e)
      {
        System.out.println("Invalid input! Please enter a number.");
      }
    }
  }
}