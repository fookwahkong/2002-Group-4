package bto.controller.password;

import bto.entity.user.User;

public interface IUserController {
    boolean updatePassword(User user, String newPassword);
}


