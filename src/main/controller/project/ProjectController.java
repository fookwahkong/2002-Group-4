package main.controller.project;

import java.util.ArrayList;
import java.util.List;

import main.entity.project.Project;
import main.utils.FileIOUtil;
import main.entity.user.User;


public class ProjectController {
    private static List<Project> projects = new ArrayList<>();
    public ProjectController() {
    }

    public static List<Project> getProjectList(User user) {
        return projects;
    } 

    public static List<Project> getProjectListFiltered(User user, int filter) {
        List<Project> projectlist = new ArrayList<>();
        switch(filter) {
            default: break;
        }
        return projectlist;
    }

    public static void load() {
        projects = FileIOUtil.loadProjects();
        for (Project p: projects) {
            System.out.println(p.getName());
        }
    }
    public Project findProject() {
        return null;
    };

}
