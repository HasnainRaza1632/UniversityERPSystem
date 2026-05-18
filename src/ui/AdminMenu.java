package ui;

import model.User;
import service.*;
import utils.InputHelper;

public class AdminMenu {

    public static void show(User user){
        while(true){
            System.out.println(
                    "========================================\n" +
                            "              ADMIN MENU               \n" +
                            "Welcome, " + user.getUsername() + "!\n" +
                            "========================================\n" +
                            "1. Manage Students\n" +
                            "2. Manage Faculty\n" +
                            "3. Manage Courses\n" +
                            "4. Manage Departments\n" +
                            "5. Manage Exams\n" +
                            "6. Assign Grade to Student\n" +
                            "7. Logout\n" +
                            "========================================");


           int choice = InputHelper.getChoice();
            switch(choice){
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
                    GradeService.assignGradeToStudents();
                    break;
                case 7:
                    System.out.println("Logged out!");
                    return;
                default:
                    System.out.println("invalid choice");
                    break;

            }
        }
    }


}




