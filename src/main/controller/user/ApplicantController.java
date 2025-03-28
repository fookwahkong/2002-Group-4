package main.controller.user;

import main.entity.user.User;
import main.controller.project.Project;
import main.controller.user.UserManager;
import main.entity.project.Enquiry;

import java.util.Scanner;

public class ApplicantController {
    public static void submitEnquiry(String message, Project project) {
        User currentUser = UserManager.getInstance().getCurrentUser();

    }
}
