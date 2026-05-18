package model;

import service.StudentService;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {

    private String regId;
    private int semester;
    private Department department;
    private List<Course> courseList;
    private List<Grade> gradesList;
    private User user;

    public Student(String name,String email,String phone,String regId,int semester,User user){
       super(name,email,phone);
       this.regId = regId;
       this.semester = semester;
       this.user = user;
       this.courseList = new ArrayList<>();
       this.gradesList = new ArrayList<>();
    }

    public String getRegId() {
        return regId;
    }

    public int getSemester() {
        return semester;
    }

    public Department getDepartment() {
        return department;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public List<Grade> getGradesList() {
        return gradesList;
    }

    public User getUser() {
        return user;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGradesList(List<Grade> gradesList) {
        this.gradesList = gradesList;
    }

    public double getGPA(){
        return StudentService.calculateGPA(this.regId);
    }

    public void viewTranscript(){
        System.out.println(StudentService.viewTranscript(this.regId));
    }


    @Override
    public String getDetails() {
        String line = "╠══════════════════════════════════════════════════════════════════════╣";
        String top =  "╔══════════════════════════════════════════════════════════════════════╗";
        String bot =  "╚══════════════════════════════════════════════════════════════════════╝";
        String title = "║                        STUDENT PROFILE                              ║";
        return "\n" + top +
                "\n" + title +
                "\n" + line +
                "\n" + formatLine("Reg ID     : ", getRegId()) +
                "\n" + formatLine("Name       : ", getName()) +
                "\n" + formatLine("Email      : ", getEmail()) +
                "\n" + formatLine("Phone      : ", getPhone()) +
                "\n" + formatLine("Semester   : ", String.valueOf(getSemester())) +
                "\n" + formatLine("Department : ", department != null ? department.getDeptName() : "Not Assigned") +
                "\n" + bot;
    }

    @Override
    public String toString() {
        return "Student{" +
                "regId='" + regId + '\'' +
                ", semester=" + semester +
                ", department=" + department +
                ", courseList=" + courseList +
                ", gradesList=" + gradesList +
                ", user=" + user +
                '}';
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
}
