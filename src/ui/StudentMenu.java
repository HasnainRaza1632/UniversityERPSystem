package ui;

import model.Course;
import model.Grade;
import model.Student;
import model.User;
import service.ExamService;
import service.StudentService;
import utils.InputHelper;

import java.util.List;

public class StudentMenu {

    // ─── Box drawing constants (72 chars wide, 70 inner) ──────────────────────
    private static final String LINE      = "╠══════════════════════════════════════════════════════════════════════╣";
    private static final String TOP       = "╔══════════════════════════════════════════════════════════════════════╗";
    private static final String BOT       = "╚══════════════════════════════════════════════════════════════════════╝";
    private static final int    BOX_WIDTH = 70;

    private static String row(String content) {
        int padding = BOX_WIDTH - content.length();
        if (padding < 0) { content = content.substring(0, BOX_WIDTH); padding = 0; }
        return "║" + content + " ".repeat(padding) + "║";
    }

    private static String center(String text) {
        int space = BOX_WIDTH - text.length();
        int left  = space / 2;
        int right = space - left;
        return "║" + " ".repeat(left) + text + " ".repeat(right) + "║";
    }

    public static void show(User user) {
        // Find the student profile linked to this user account
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

        boolean running = true;
        while (running) {
            System.out.println(TOP);
            System.out.println(center("STUDENT MENU"));
            System.out.println(LINE);
            System.out.println(row("  Welcome, " + student.getName() + "!"));
            System.out.println(row(""));
            System.out.println(row("   [ 1 ]  View My Profile"));
            System.out.println(row("   [ 2 ]  View My Enrolled Courses"));
            System.out.println(row("   [ 3 ]  View My Grades"));
            System.out.println(row("   [ 4 ]  View Transcript & GPA"));
            System.out.println(row("   [ 5 ]  View Exam Schedule"));
            System.out.println(row("   [ 6 ]  Logout"));
            System.out.println(BOT);
            System.out.println();

            int choice = InputHelper.getChoice();
            switch (choice) {
                case 1:
                    System.out.println(student.getDetails());
                    break;
                case 2:
                    viewCourses(student);
                    break;
                case 3:
                    viewGrades(student);
                    break;
                case 4:
                    student.viewTranscript();
                    break;
                case 5:
                    ExamService.viewAllExams();
                    break;
                case 6:
                    System.out.println("Logged out.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void viewCourses(Student student) {
        List<Course> courses = student.getCourseList();
        if (courses.isEmpty()) {
            System.out.println("No courses enrolled yet.");
            return;
        }
        System.out.println("-------- MY ENROLLED COURSES --------");
        System.out.printf("%-10s | %-30s | %s%n", "ID", "Course Name", "Credits");
        System.out.println("-".repeat(52));
        for (Course course : courses) {
            System.out.printf("%-10s | %-30s | %d credits%n",
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCreditHours());
        }
        System.out.println("-".repeat(52));
    }

    private static void viewGrades(Student student) {
        List<Grade> grades = student.getGradesList();
        if (grades.isEmpty()) {
            System.out.println("No grades available yet.");
            return;
        }
        System.out.println("-------- MY GRADES --------");
        for (Grade grade : grades) {
            System.out.println(grade.getDetails());
        }
        System.out.println("---------------------------");
    }
}
