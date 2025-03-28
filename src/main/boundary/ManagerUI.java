package main.boundary;

import main.controller.project.ProjectController;
import main.controller.user.UserManager;
import main.entity.user.HDBManager;
import main.entity.user.User;

import java.util.Scanner;

public class ManagerUI {
    private static final Scanner scanner = new Scanner(System.in);
    private final User currentUser = UserManager.getInstance().getCurrentUser();

    public void showMenu() {
        System.out.println("MANAGER UI");
        System.out.println("==================================");
        System.out.println("1. Create HDB Project");
        System.out.println("2. Edit HDB Project");
        System.out.println("3. Delete HDB Project");


        int choice = getValidIntInput();

        switch (choice) {
            case 1 -> createHDBProject();
            case 2 -> editHDBProject();
            case 3 -> deleteHDBProject();
            default -> System.out.println("Invalid choice! Please enter a number between 1 and 5");
        }
    }

    private void createHDBProject() {
        System.out.println("Enter project name: ");
        String projectName = scanner.nextLine();
        System.out.println("Enter neighbourhood: ");
        String neighbourHood = scanner.nextLine();
        System.out.println("Enter type 1 price: ");
        float priceOne = Float.parseFloat(scanner.nextLine());
        System.out.println("Enter number of units for 2-Room: ");
        int noOfUnitsOne = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter type 2 price: ");
        float priceTwo = Float.parseFloat(scanner.nextLine());
        System.out.println("Enter number of units for 3-Room: ");
        int noOfUnitsTwo = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter opening date (mm/dd/yyyy): ");
        String openingDate = scanner.nextLine();
        System.out.println("Enter closing date (mm/dd/yyyy): ");
        String closingDate = scanner.nextLine();
        System.out.println("Enter number of officer slots: ");
        int slots = Integer.parseInt(scanner.nextLine());
        ProjectController.createProject(projectName, neighbourHood, priceOne, noOfUnitsOne, priceTwo, noOfUnitsTwo, openingDate, closingDate, (HDBManager) currentUser, slots);
    };

    private void editHDBProject() {};

    private void deleteHDBProject() {};

    private int getValidIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.next(); // Clear invalid input
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return input;
    }
}
