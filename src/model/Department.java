package model;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String deptId;
    private String deptName;
    private String headOfDepartment;
    private List<Course> courses;
    private List<Faculty> facultyList;

    public Department(String deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.courses = new ArrayList<>();
        this.facultyList = new ArrayList<>();
    }

    // Getters
    public String getDeptId() {
        return deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Faculty> getFacultyList() {
        return facultyList;
    }

    // Setters
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    // Business methods
    public void addCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
            course.setDepartment(this);
        }
    }

    public void addFaculty(Faculty faculty) {
        if (faculty != null && !facultyList.contains(faculty)) {
            facultyList.add(faculty);
            faculty.setDepartment(this);
        }
    }

    public void removeCourse(Course course) {
        if(course != null){
            courses.remove(course);
        }
    }

    public void removeFaculty(Faculty faculty) {
        if(faculty != null){
            facultyList.remove(faculty);
        }
    }


    public String getDetails() {
        String line = "╠══════════════════════════════════════════════════════════════════════╣";
        String top =  "╔══════════════════════════════════════════════════════════════════════╗";
        String bot =  "╚══════════════════════════════════════════════════════════════════════╝";
        String title = "║                          DEPARTMENT DETAILS                          ║";
        return top +
                "\n" + title +
                "\n" + line +
                "\n" + formatLine("Dept ID  : ", getDeptId()) +
                "\n" + formatLine("Name     : ", getDeptName()) +
                "\n" + formatLine("HOD      : ", getHeadOfDepartment() != null ? getHeadOfDepartment() : "Not Assigned") +
                "\n" + bot;
    }
    private static String formatLine(String label, String value) {
        String content = label + value;
        int totalWidth = 68;
        int padding = totalWidth - content.length();
        if (padding < 0) {
            content = content.substring(0, totalWidth - 3) + "...";
            padding = 0;
        }
        return "║  " + content + " ".repeat(padding) + "  ║";
    }

    @Override
    public String toString() {
        return deptId + "," + deptName + "," + (headOfDepartment != null ? headOfDepartment : "");
    }
}
