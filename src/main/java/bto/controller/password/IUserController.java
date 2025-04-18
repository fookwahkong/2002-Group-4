package bto.controller.password;

import bto.entity.user.User;

public interface IUserController {
    /**
     * Update the password.
     * 
     * @param user the user
     * @param newPassword the new password
     * @return true if the password is updated, false otherwise
     */
    boolean updatePassword(User user, String newPassword);
}


