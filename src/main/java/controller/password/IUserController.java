package controller.password;

import entity.user.User;

public interface IUserController {
    boolean updatePassword(User user, String newPassword);
}


