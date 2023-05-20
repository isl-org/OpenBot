package org.openbot.projects;

import java.util.ArrayList;

public class DriveProjectList {
  public ArrayList<ProjectsDataInObject> projectsList;
  private static DriveProjectList instance;

  private DriveProjectList() {
    // Private constructor to prevent direct instantiation
    projectsList = new ArrayList<>();
  }

  public static DriveProjectList getInstance() {
    if (instance == null) {
      synchronized (DriveProjectList.class) {
        if (instance == null) {
          instance = new DriveProjectList();
        }
      }
    }
    return instance;
  }

  /**
   * getter method get all projects data in object.
   *
   * @return The list of project data object.
   */
  public ArrayList<ProjectsDataInObject> getAllProjects() {
    return projectsList;
  }
}
