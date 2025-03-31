package main.controller.enquiry;

import java.util.*;
import main.entity.Enquiry;
import main.entity.user.User;

public class EnquiryController {

    private static List<Enquiry> enquiries = new ArrayList<>();

    public static List<Enquiry> getEnquiriesList(User user) {
        return enquiries;
    } 

    public static List<Enquiry> getEnquiriesByOfficer(User user) {
        return EnquiryController.getEnquiriesList(user).stream()
            .filter(enquiry -> enquiry.getApplicant().getName().equals(user.getName()))
            .toList();
    }

    public static void replyToEnquiry(Enquiry selectedEnquiry, String reply) {
        selectedEnquiry.setReply(reply);
    }
}
