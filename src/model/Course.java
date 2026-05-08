package model;

import java.util.List;

public class Course {
    private String courseId;
    private String courseName;
    private int creditHours;
    private String description;
    private Faculty assignedFaculty;
    private Department department;

    public Course(String courseId , String courseName , int creditHours , String description , Faculty assignedFaculty , Department department){
        this.courseId = courseId;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.description = description;
        this.assignedFaculty = assignedFaculty;
        this.department = department;
    }
    // Getters
    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public String getDescription() {
        return description;
    }

    public Faculty getAssignedFaculty() {
        return assignedFaculty;
    }

    public Department getDepartment() {
        return department;
    }

    // Setters
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAssignedFaculty(Faculty assignedFaculty) {
        this.assignedFaculty = assignedFaculty;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getDetails() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", creditHours=" + creditHours +
                ", faculty=" + (assignedFaculty != null ? assignedFaculty.getName() : "Not Assigned") +
                '}';
    }

    @Override
    public String toString() {
        return courseId + "," + courseName + "," + creditHours + "," + description;
    }
}
