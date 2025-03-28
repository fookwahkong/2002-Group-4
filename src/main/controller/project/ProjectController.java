package main.controller.project;

import java.util.ArrayList;
import java.util.List;

import main.entity.project.Project;
import main.utils.FileIOUtil;


public class ProjectController {
    private static List<Project> projects = new ArrayList<>();
    public ProjectController() {
    }

    public static List<Project> getProjectList() {
        return projects;
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
