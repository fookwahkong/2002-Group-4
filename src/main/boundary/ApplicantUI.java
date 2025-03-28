package main.boundary;

import main.controller.user.ApplicantController;

import java.util.Scanner;

public class ApplicantUI {
    private static final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        System.out.println("APPLICANT UI");
        System.out.println("==================================");
        System.out.println("5. Submit Enquiry");


        int choice = getValidIntInput();

        switch (choice) {
            case 5 -> ApplicantController.submitEnquiry();
            default -> System.out.println("Invalid choice! Please enter a number between 1 and 5");
        }
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
