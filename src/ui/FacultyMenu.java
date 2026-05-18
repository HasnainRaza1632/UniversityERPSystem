package ui;

import model.Faculty;
import model.User;
import service.FacultyService;

import utils.InputHelper;

import java.util.List;

public class FacultyMenu {

    public static void show(User user){
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
        while(true){
            System.out.println("========================================\n" +
                    "            FACULTY MENU               \n" +
                    "Welcome, " + faculty.getName() + "!\n" +
                    "========================================\n" +
                    "1. View My Assigned Courses\n" +
                    "2. View My Profile\n" +
                    "3. Logout\n" +
                    "========================================");

            int choice = InputHelper.getChoice();
            switch (choice){
                case 1:
                    faculty.viewAssignedCourses();
                    break;
                case 2:
                    System.out.println(faculty.getDetails());
                    break;
                case 3:
                    System.out.println("Logged out");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }

    }

}
