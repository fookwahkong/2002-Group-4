package main.boundary;

import java.util.Scanner;

import main.controller.PasswordController;

public class ChangePasswordUI {

    private static Scanner scanner = new Scanner(System.in);
    private PasswordController passwordController;           

    public ChangePasswordUI() {
        passwordController = new PasswordController();
    }

    public void showChangePasswordMenu() {
        String content = """
                ==================================================================================
                BTO MANAGEMENT SYSTEM CHANGE PASSWORD
                Information on Changing Password
                ==================================================================================
                1. At least 8 characters long
                2. Include at least one uppercase letter
                3. Include at lease one lowercase letter
                4. Include at least one number
                5. Include at least one special character (e.g., !, @, #, $, %, ^, &, *)
                ==================================================================================

                Enter your new password:
                """;

        System.out.println(content);

        String password = scanner.nextLine().trim();

        // Implement password validation
        boolean passwordChange = passwordController.isPasswordValid(password);

        if (passwordChange) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Password change failed. Please try again.");
        }

    }
}
