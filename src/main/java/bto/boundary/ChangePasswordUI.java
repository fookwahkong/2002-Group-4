package bto.boundary;

import bto.controller.password.IUserController;
import bto.controller.user.UserManager;
import bto.entity.user.User;

/**
 * A class representing a change password UI.
 */
public class ChangePasswordUI {

    private final IUserController userController;

    /**
     * Constructor for the ChangePasswordUI class.
     * 
     * @param userController the user controller
     */
    public ChangePasswordUI(IUserController userController) {
        this.userController = userController;
    }

    /**
     * Display the menu options.
     */
    private void displayMenuOptions() {
        String[] menuOptions = {
                "==================================================================================",
                "BTO MANAGEMENT SYSTEM CHANGE PASSWORD",
                "Information on Changing Password",
                "==================================================================================",
                "1. At least 8 characters long",
                "2. Include at least one uppercase letter",
                "3. Include at lease one lowercase letter",
                "4. Include at least one number",
                "5. Include at least one special character (e.g., !, @, #, $, %, ^, &, *)",
                "==================================================================================" };
        UIUtils.displayMenuOptions(menuOptions);
    }

    /**
     * Display the change password menu.
     */ 
    protected void displayChangePasswordMenu() {
        User currentUser = UserManager.getInstance().getCurrentUser();

        if (currentUser == null) {
            System.out.println("No user is currently logged in");
            return;
        }

        boolean successful = false;
        while (!successful) {
            displayMenuOptions();
            String password = UIUtils.getStringInput("Enter your new password: ");

            successful = userController.updatePassword(currentUser, password);

            if (successful) {
                System.out.println("Password changed successfully!");
            } else {
                System.out.println("Password change failed. Please ensure it meets all requirements.\n");

            }

        }
    }
}
