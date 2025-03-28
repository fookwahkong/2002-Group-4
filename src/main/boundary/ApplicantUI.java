package main.boundary;

import main.controller.user.ApplicantController;
import main.controller.project.ProjectController;
import main.entity.project.Project;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ApplicantUI {
    private static final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        System.out.println("APPLICANT UI");
        System.out.println("==================================");
        System.out.println("5. Submit Enquiry");


        int choice = getValidIntInput();

        switch (choice) {
            case 5 -> submitEnquiry();
            default -> System.out.println("Invalid choice! Please enter a number between 1 and 5");
        }
    }

    public void submitEnquiry() {
        // go to ProjectController to get list of projects
        System.out.println("List of visible projects:");
        List<Project> projectList = ProjectController.getProjectList();
        int cnt = 1;
        for (Project p: projectList) {
            System.out.print(cnt + ". ");
            System.out.println(p.getName());
            cnt += 1;
        }
        System.out.println("Select the project to submit enquiry for: ");

        int projIndex = getValidIntInput() - 1;
        Project proj = projectList.get(projIndex);

        // take in input;
        System.out.print("Enter enquiry message: ");
        String message = scanner.nextLine();

        // selected project will be pass to ApplicantController
        ApplicantController.submitEnquiry(message, proj);
    }
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
