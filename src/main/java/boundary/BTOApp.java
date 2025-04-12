package boundary;

import controller.enquiry.EnquiryController;
import controller.project.ProjectController;
import controller.user.UserManager;
import utils.FileIOUtil;
import controller.registration.RegistrationController;

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