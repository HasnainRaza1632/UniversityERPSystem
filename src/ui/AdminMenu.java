package ui;

import model.User;
import service.*;
import utils.InputHelper;

public class AdminMenu {

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
        while (true) {
            System.out.println(TOP);
            System.out.println(center("ADMIN MENU"));
            System.out.println(LINE);
            System.out.println(row("  Welcome, " + user.getUsername() + "!"));
            System.out.println(row(""));
            System.out.println(row("   [ 1 ]  Manage Students"));
            System.out.println(row("   [ 2 ]  Manage Faculty"));
            System.out.println(row("   [ 3 ]  Manage Courses"));
            System.out.println(row("   [ 4 ]  Manage Departments"));
            System.out.println(row("   [ 5 ]  Manage Exams"));
            System.out.println(row("   [ 6 ]  View All Grades"));
            System.out.println(row("   [ 7 ]  Update a Grade"));
            System.out.println(row("   [ 8 ]  Logout"));
            System.out.println(BOT);
            System.out.println();

            int choice = InputHelper.getChoice();
            switch (choice) {
                case 1:
                    StudentService.manageStudents();
                    break;
                case 2:
                    FacultyService.manageFaculty();
                    break;
                case 3:
                    CourseService.manageCourses();
                    break;
                case 4:
                    DepartmentService.manageDepartments();
                    break;
                case 5:
                    ExamService.manageExams();
                    break;
                case 6:
                    GradeService.viewAllGrades();
                    break;
                case 7:
                    GradeService.updateGradeConsole();
                    break;
                case 8:
                    System.out.println("Logged out!");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}
