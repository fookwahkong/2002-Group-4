package controller.user;

import entity.user.User;

public class PasswordController {

    public static boolean updatePassword(User user, String newPassword) {
        if (isPasswordValid(newPassword)) {
            user.changePassword(newPassword);
            UserManager.save();
            return true;
        }
        return false;
    }

    public static boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        String specialCharacters = "!@#$%^&*";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialCharacters.contains(String.valueOf(c))) {
                hasSpecialChar = true;
            }
        }

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
}
