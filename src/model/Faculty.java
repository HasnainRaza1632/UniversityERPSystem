package model;

import java.util.ArrayList;
import java.util.List;

public class Faculty extends Person{
    private String facultyId;
    private String designation;
    private Department department;
    private List<Course> assignedCourses;
    private double salary;
    private User user;

    public Faculty(String name, String email , String phone ,String employeeId , String designation , Department department , double salary , User user){
        super(name, email, phone);
        this.facultyId = employeeId;
        this.designation = designation;
        this.salary = salary;
        this.department = department;
        this.user = user;
        assignedCourses = new ArrayList<>();
    }

    public String getFacultyId() {
        return facultyId;
    }

    public String getDesignation() {
        return designation;
    }

    public double getSalary() {
        return salary;
    }

    public Department getDepartment() {
        return department;
    }

    public List<Course> getAssignedCourses() {
        return assignedCourses;
    }

    // Setters
    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean assignCourse(Course course){
        if(course != null && !assignedCourses.contains(course)){
            assignedCourses.add(course);
            return true;
        }
        return false;
    }

    public boolean removeAssignCourse(Course course){
        if(assignedCourses.contains(course)){
            assignedCourses.remove(course);
            return true;
        }
        return false;
    }

    public void viewAssignedCourses(){
        System.out.println("\n========== ASSIGNED COURSES ==========");
        System.out.println("Faculty: " + getName());
        System.out.println("Designation: " + designation);
        System.out.println("======================================");

        if (assignedCourses.isEmpty()) {
            System.out.println("No courses assigned.");
        } else {
            for (Course course : assignedCourses) {
                System.out.println(course.getDetails());
            }
        }
        System.out.println("======================================\n");
    }

    @Override
    public String getDetails() {
        return "Faculty{" +
                "name='" + getName() + '\'' +
                ", facultyId='" + facultyId + '\'' +
                ", designation='" + designation + '\'' +
                ", department=" + (department != null ? department.getDeptName() : "Not Assigned") +
                ", assignedCourses=" + assignedCourses.size() +
                '}';
    }

    @Override
    public String toString() {
        return facultyId + "," + getName() + "," + designation + "," + getEmail();
    }
}
