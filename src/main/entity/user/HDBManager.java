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

    public void approveOfficer(HDBOfficer officer) {
        officer.approve();
        pendingApprovals.remove(officer);
        ProjectController.addApprovedOfficer(officer);
    }

    public void rejectOfficer(HDBOfficer officer) {
        pendingApprovals.remove(officer);
}


  
  
