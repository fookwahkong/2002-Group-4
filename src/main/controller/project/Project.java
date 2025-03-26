package main.controller.project;

import java.util.Date;

import main.controller.Application;
import main.controller.user.Registration;
import main.entity.project.ProjectDetails;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.entity.Housing;
import main.entity.project.Enquiry;

public class Project extends ProjectDetails {
    public Project(String name, boolean visible, String neighborhood, Date openingDate, Date closingDate,
                   HDBManager manager, HDBOfficer officers, Application[] applications, Enquiry[] enquiries,
                   Registration[] registrations, Housing housingType, int numberofHousing) {
        super(name, neighborhood, visible);
    }
    
}
