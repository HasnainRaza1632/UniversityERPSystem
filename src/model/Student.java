package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person{
    private String regId;
    private int semester;
    private Department department;
    private List<Course> courseList;
    private List<Grade> gradesList;
    private User user;

    public Student(String name, String email, String phone, String regId, int semester, User user){
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

    // Setters


    public void setGradesList(List<Grade> gradesList) {
        this.gradesList = gradesList;
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

    public void setDepartment(Department department) {
        this.department = department;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //Logic Methods
    public boolean enrollCourse(Course course){
        if(course != null && !courseList.contains(course)){
            courseList.add(course);
            return true;
        }
        return false;
    }

    public double getGPA(){
        if(gradesList.isEmpty()){
            return 0.0;
        }
        double totalPoints = 0.0;
        int totalCreditHours = 0;

        for (Grade grade : gradesList) {
            totalPoints += grade.getGradePoint() * grade.getCourse().getCreditHours();
            totalCreditHours += grade.getCourse().getCreditHours();
        }

        return totalCreditHours > 0 ? totalPoints / totalCreditHours : 0.0;
    }

    public void viewTranscript(){
        System.out.println("\n========== TRANSCRIPT ==========");
        System.out.println("Student: " + getName());
        System.out.println("Reg ID: " + regId);
        System.out.println("Semester: " + semester);
        System.out.println("================================");

        if (gradesList.isEmpty()) {
            System.out.println("No grades available.");
        } else {
            for (Grade grade : gradesList) {
                System.out.println(grade.getDetails());
            }
            System.out.println("--------------------------------");
            System.out.printf("GPA: %.2f\n", getGPA());
        }
        System.out.println("================================\n");

    }

    @Override
    public String getDetails() {
        return "Student{" +
                "name='" + getName() + '\'' +
                ", regId='" + regId + '\'' +
                ", semester=" + semester +
                ", email='" + getEmail() + '\'' +
                ", GPA=" + String.format("%.2f", getGPA()) +
                '}';
    }

    @Override
    public String toString() {
        return regId + "," + getName() + "," + semester + "," + getEmail();
    }

}
