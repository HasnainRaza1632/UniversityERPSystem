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
        }
    }

    public void removeCourse(Course course) {
        if(course != null && courses.remove(course)){
            courses.remove(course);
        }
    }

    public void removeFaculty(Faculty faculty) {
        if(faculty != null && facultyList.remove(faculty)){
            facultyList.remove(faculty);
        }
    }

    public String getDetails() {
        return "Department{" +
                "deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", HOD='" + (headOfDepartment != null ? headOfDepartment : "Not Assigned") + '\'' +
                ", courses=" + courses.size() +
                ", faculty=" + facultyList.size() +
                '}';
    }

    @Override
    public String toString() {
        return deptId + "," + deptName + "," + (headOfDepartment != null ? headOfDepartment : "");
    }
}
