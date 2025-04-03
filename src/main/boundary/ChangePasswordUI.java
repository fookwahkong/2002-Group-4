package main.boundary;

import main.controller.user.PasswordController;
import main.controller.user.UserManager;
import main.entity.user.User;

public class ChangePasswordUI extends UI {

    private PasswordController passwordController;

    public ChangePasswordUI() {
        passwordController = new PasswordController();
    }

    public void showChangePasswordMenu() {
        User currentUser = UserManager.getInstance().getCurrentUser();

        if (currentUser == null) {
            System.out.println("No user is currently logged in");
            return;
        }

        boolean successful = false;
        while (!successful) {

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

            String password = getStringInput(content);

            successful = passwordController.updatePassword(currentUser, password);

            if (successful) {
                System.out.println("Password changed successfully!");
            } else {
                System.out.println("Password change failed. Please ensure it meets all requirements.");

            }

        }
    }
}
