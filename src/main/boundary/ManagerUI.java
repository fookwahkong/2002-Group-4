package main.boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import main.controller.enquiry.EnquiryController;
import main.controller.project.ProjectController;
import main.controller.user.UserManager;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.HDBManager;
import main.entity.user.User;
import main.enums.UserRole;

public class ManagerUI {
  private static final Scanner scanner = new Scanner(System.in);
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
  private final HDBManager currentUser;
  private ChangePasswordUI changePasswordUI = new ChangePasswordUI();

  public ManagerUI() {
    User user = UserManager.getInstance().getCurrentUser();

    // downcasting from user to manager
    if (user != null && user.getUserRole() == UserRole.HDB_MANAGER) {
      this.currentUser = (HDBManager) user;
    } else {
      throw new IllegalStateException("Current user is not an HDB Manager");
    }
  }

  public void showMenu() {
    while (true) {
      displayMenuOptions();

      try {
        int choice = getValidIntInput(0, 7);

        switch (choice) {
          case 1 -> viewProjects();
          case 2 -> createHDBProject();
          case 3 -> editHDBProject();
          case 4 -> deleteHDBProject();
          case 5 -> viewAllEnquiries();
          case 6 -> viewAndReplyToEnquiries();
          case 7 -> changePasswordUI.showChangePasswordMenu();
          case 0 -> new LoginUI().navigateToLoginMenu();
        }
      } catch (Exception e) {
        System.out.println("An error occurred: " + e.getMessage());
      }
    }
  }

  private void displayMenuOptions() {
    System.out.println("\nMANAGER UI");
    System.out.println("==================================");
    System.out.println("1. View projects");
    System.out.println("2. Create HDB Project");
    System.out.println("3. Edit HDB Project");
    System.out.println("4. Delete HDB Project");
    System.out.println("5. View all enquiries");
    System.out.println("6. View and reply enquiries on projects you handle");
    System.out.println("7. Change Password");
    System.out.println("0. Logout");
    System.out.print("Enter your choice: ");
  }

  private void viewProjects() {
    int filterChoice = getIntInput("Select projects to view (1. All 2. Self): ");
    switch (filterChoice) {
      case 1 -> {
        for (Project p : ProjectController.getProjectList()) {
          System.out.println(p.getName());
        }
      }
      case 2 -> {
        for (Project p : ProjectController.getManagerProjects(currentUser)) {
          System.out.println(p.getName());
        }
      }
    }

  }

  private void createHDBProject() {
    try {
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
          slots);

      System.out.println("HDB Project created successfully!");
    } catch (Exception e) {
      System.out.println("Failed to create HDB Project: " + e.getMessage());
    }
  }

  private void editHDBProject() {
    System.out.println("Edit HDB Project functionality not implemented yet.");
    // TODO: Implement project editing logic
  }

  private void deleteHDBProject() {
    List<Project> projectList = ProjectController.getProjectList();
    int cnt = 1;
    for (Project p : projectList) {
      System.out.print(cnt + ". ");
      System.out.println(p.getName());
      cnt += 1;
    }

    int projIndex = getIntInput("Select the project to submit enquiry for: ") - 1;
    Project proj = projectList.get(projIndex);
    ProjectController.deleteProject(proj);
  }

  private void viewAllEnquiries() {
    List<Enquiry> enquiryList = EnquiryController.getEnquiriesList(currentUser);
    
    if (enquiryList.isEmpty()) {
      System.out.println("No enquiries found.");
      return;
    }
    
    int cnt = 1;
    for (Enquiry e : enquiryList) {
        System.out.print(cnt + ". ");
        e.viewEnquiry("manager");
        cnt++;  // Increment the counter
        System.out.println(); // Add a newline for better formatting
    }
  }

  private void viewAndReplyToEnquiries() {
    List<Enquiry> enquiries = EnquiryController.getEnquiriesByManager(currentUser);
    if (enquiries.isEmpty()) {
      System.out.println("No enquiries assigned to you.");
      return;
    }

    System.out.println("Enquiries you are handling:");
    int index = 1;
    for (Enquiry enquiry : enquiries) {
      System.out.println(index++ + ". " + enquiry.getContent());
    }

    int enquiryIndex = getIntInput("Select an enquiry to reply (0 to cancel): ") - 1;
    if (enquiryIndex < 0 || enquiryIndex >= enquiries.size()) {
      System.out.println("Returning to menu.");
      return;
    }

    Enquiry selectedEnquiry = enquiries.get(enquiryIndex);
    String reply = getStringInput("Enter your reply: ");
    EnquiryController.replyToEnquiry(selectedEnquiry, reply);
    System.out.println("Reply sent successfully!");
  }

  private String getStringInput(String prompt) {
    System.out.print(prompt);
    return scanner.nextLine().trim();
  }

  private int getIntInput(String prompt) {
    while (true) {
      try {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid integer.");
      }
    }
  }

  private float getFloatInput(String prompt) {
    while (true) {
      try {
        System.out.print(prompt);
        return Float.parseFloat(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
  }

  private LocalDate getDateInput(String prompt) {
    while (true) {
      try {
        System.out.print(prompt);
        return LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
      } catch (DateTimeParseException e) {
        System.out.println("Invalid date format. Please use mm/dd/yyyy.");
      }
    }
  }

  private int getValidIntInput(int min, int max) {
    while (true) {
      try {
        int input = Integer.parseInt(scanner.nextLine());
        if (input >= min && input <= max) {
          return input;
        }
        System.out.printf("Please enter a number between %d and %d%n", min, max);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input! Please enter a number.");
      }
    }
  }
}