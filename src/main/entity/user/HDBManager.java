package main.entity.user;

import main.enums.MaritalStatus;
import main.enums.UserRole;

public class HDBManager extends User{
    private List<HDBOfficer> pendingApprovals = new ArrayList<>();
    private Project managedProject;
    public HDBManager(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    public List<HDBOfficer> getPendingApprovals() {
        return pendingApprovals;
    }

    public void addPendingApproval(HDBOfficer officer) {
        pendingApprovals.add(officer);
    }

    public void approveOfficer(HDBOfficer officer, Project project) {
        if (project.getPendingOfficers().contains(officer)) {
            officer.assignProject(project);
            project.getApprovedOfficers().add(officer);
            project.getPendingOfficers().remove(officer);
            System.out.println(officer.getName() + " has been approved for project " + project.getName());
        } else {
            System.out.println("Officer is not in pending list.");
        }
    }


    public void rejectOfficer(HDBOfficer officer, Project project) {
        if (project.getPendingOfficers().contains(officer)) {
            project.getPendingOfficers().remove(officer);
            System.out.println(officer.getName() + " has been rejected for project " + project.getName());
        } else {
            System.out.println("Officer is not in pending list.");
        }
    }

}


  
  
