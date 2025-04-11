package main.entity.user;

import java.util.HashMap;
import java.util.Map;

import main.entity.project.Project;
import main.enums.MaritalStatus;
import main.enums.UserRole;
import main.enums.ProjectStatus;

public class Applicant extends User {

    public Applicant(String userID, String password, String name, int age, MaritalStatus maritalStatus,
            UserRole userRole ) {
        super(userID, password, name, age, maritalStatus, userRole);
    }

    public String getName() {
        return super.getName();
    }

    public int getAge() {
        return super.getAge();
    }
}
