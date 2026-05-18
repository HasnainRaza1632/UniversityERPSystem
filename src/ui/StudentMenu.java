package ui;

import model.Course;
import model.Grade;
import model.Student;
import model.User;
import service.StudentService;
import utils.InputHelper;

import java.util.List;

public class StudentMenu {



    public static void show(User user){

        //check if the student exist or not
         Student student = null;
        List<Student> allStudents = StudentService.getAllStudents();
        for (Student s : allStudents) {
            if (s.getUser() != null && s.getUser().getUsername().equals(user.getUsername())) {
                student = s;
                break;
            }
        }
        if (student == null) {
            System.out.println("Profile not found. Contact admin.");
            return;
        }
     //show the menu
        boolean running = true;
     while(running){
         System.out.println("========================================\n" +
                 "             STUDENT MENU              \n" +
                 "Welcome, " + student.getName() + "!\n" +
                 "========================================\n" +
                 "1. View My Enrolled Courses\n" +
                 "2. View My Grades\n" +
                 "3. View Transcript & GPA\n" +
                 "4. Logout\n" +
                 "========================================");
         int choice = InputHelper.getChoice();
         switch (choice){
             case 1:
                 viewCourses(student);
                 break;
             case 2:
                 viewGrades(student);
                 break;
             case 3:
                 student.viewTranscript();
                 break;
             case 4:
                 System.out.println("Logged out.");
                 running = false;
                 break;
             default:
                 System.out.println("Invalid option.");

         }
     }

    }

    private  static void viewCourses(Student student){
        List<Course> courses = student.getCourseList();
        if(courses.isEmpty()){
            System.out.println("No courses enrolled yet.");
            return;
        }
        System.out.println("-------- MY ENROLLED COURSES --------");
        System.out.printf("%-10s | %-30s | %s%n", "ID", "Course Name", "Credits");
        System.out.println("-".repeat(52));
        for (Course course : courses){
            System.out.printf("%-10s | %-30s | %d credits%n",
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCreditHours());
        }
        System.out.println("-".repeat(52));
    }
    private static void viewGrades(Student student){
        List<Grade> grades = student.getGradesList();
        if(grades.isEmpty()){
            System.out.println("No grades available yet.");
            return;
        }
        for (Grade grade : grades) {
            System.out.println(grade.getDetails());
        }
    }

}
