package bto.boundary;

import bto.controller.enquiry.EnquiryController;
import bto.controller.project.ProjectController;
import bto.controller.user.UserManager;
import bto.utils.FileIOUtil;
import bto.controller.registration.RegistrationController;

public class BTOApp {
    public static void start() {

        UserManager.load();
        ProjectController.load();
        EnquiryController.load();
        RegistrationController.load();
        FileIOUtil.loadBookingDetails(ProjectController.getProjectList());
        
        LoginUI loginUI = new LoginUI();
        loginUI.startLogin();
    }
}