package ui;

import model.Exam;
import model.Faculty;
import model.Student;
import model.User;
import service.ExamService;
import service.FacultyService;
import service.GradeService;
import service.StudentService;
import utils.InputHelper;

import java.util.List;

public class FacultyMenu {

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
        // Find the faculty profile linked to this user account
        Faculty faculty = null;
        List<Faculty> allFaculties = FacultyService.getAllFaculties();
        for (Faculty f : allFaculties) {
            if (f.getUser() != null && f.getUser().getUsername().equals(user.getUsername())) {
                faculty = f;
                break;
            }
        }
        if (faculty == null) {
            System.out.println("Profile not found. Contact admin.");
            return;
        }

        while (true) {
            System.out.println(TOP);
            System.out.println(center("FACULTY MENU"));
            System.out.println(LINE);
            System.out.println(row("  Welcome, " + faculty.getName() + "!"));
            System.out.println(row(""));
            System.out.println(row("   [ 1 ]  View My Profile"));
            System.out.println(row("   [ 2 ]  View My Assigned Courses"));
            System.out.println(row("   [ 3 ]  View All Students"));
            System.out.println(row("   [ 4 ]  View Exam Schedule"));
            System.out.println(row("   [ 5 ]  Assign Grade to Student"));
            System.out.println(row("   [ 6 ]  Logout"));
            System.out.println(BOT);
            System.out.println();

            int choice = InputHelper.getChoice();
            switch (choice) {
                case 1:
                    System.out.println(faculty.getDetails());
                    break;
                case 2:
                    faculty.viewAssignedCourses();
                    break;
                case 3:
                    viewAllStudents();
                    break;
                case 4:
                    ExamService.viewAllExams();
                    break;
                case 5:
                    GradeService.assignGradeToStudents();
                    break;
                case 6:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Shows all registered students — useful before assigning grades. */
    private static void viewAllStudents() {
        List<Student> students = StudentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("-------- ALL STUDENTS --------");
        for (Student s : students) {
            System.out.println(s.getDetails());
        }
    }
}
