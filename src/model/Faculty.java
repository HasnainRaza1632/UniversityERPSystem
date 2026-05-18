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
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                          ASSIGNED COURSES                            ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        
        String facLine = "Faculty: " + getName();
        int paddingFac = 68 - facLine.length();
        System.out.println("║  " + facLine + " ".repeat(Math.max(0, paddingFac)) + "  ║");
        
        String desLine = "Designation: " + designation;
        int paddingDes = 68 - desLine.length();
        System.out.println("║  " + desLine + " ".repeat(Math.max(0, paddingDes)) + "  ║");
        
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");

        if (assignedCourses.isEmpty()) {
            System.out.println("║  No courses assigned.                                                ║");
        } else {
            for (Course course : assignedCourses) {
                System.out.println(course.getDetails());
            }
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }


//    public String getDetails() {
//        return "Faculty{" +
//                "name='" + getName() + '\'' +
//                ", facultyId='" + facultyId + '\'' +
//                ", designation='" + designation + '\'' +
//                ", department=" + (department != null ? department.getDeptName() : "Not Assigned") +
//                ", assignedCourses=" + assignedCourses.size() +
//                '}';
//    }
    @Override
    public String getDetails() {
        String line = "╠══════════════════════════════════════════════════════════════════════╣";
        String top =  "╔══════════════════════════════════════════════════════════════════════╗";
        String bot =  "╚══════════════════════════════════════════════════════════════════════╝";
        String title = "║                           FACULTY PROFILE                            ║";
        return top +
                "\n" + title +
                "\n" + line +
                "\n" + formatLine("Faculty ID  : ", getFacultyId()) +
                "\n" + formatLine("Name        : ", getName()) +
                "\n" + formatLine("Email       : ", getEmail()) +
                "\n" + formatLine("Phone       : ", getPhone()) +
                "\n" + formatLine("Designation : ", getDesignation()) +
                "\n" + formatLine("Salary      : ", String.valueOf(getSalary())) +
                "\n" + formatLine("Department  : ", getDepartment() != null ? getDepartment().getDeptName() : "Not Assigned") +
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
        return facultyId + "," + getName() + "," + designation + "," + getEmail();
    }
}
