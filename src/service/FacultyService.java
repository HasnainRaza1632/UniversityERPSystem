package service;

import dao.DepartmentDAO;
import dao.FacultyDAO;
import dao.UserDAO;
import model.Department;
import model.Faculty;
import model.User;
import utils.InputHelper;
import validation.FacultyValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FacultyService {

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

    /**
     * Add a new faculty member
     */
    public static boolean addFaculty(String name, String email, String phone, String facultyId, String designation, double salary, String departmentId, String username,String password) {
        String validationError = FacultyValidator.validate(name, email, phone, facultyId, designation, salary, username, password);
        if (validationError != null) {
            return false;
        }

        Map<String, Faculty> facultyMap = FacultyDAO.getAllFaculties();
        Map<String, User> userMap = UserDAO.getAllUsers();

        if (facultyMap.containsKey(facultyId.trim())) {
            return false; // Faculty ID already exists
        }
        if(userMap.containsKey(username.trim())){
            return false;
        }

        User user = new User(username.trim().toLowerCase(), password.trim(), "Faculty");
        userMap.put(username.trim(),user);

        Department department = DepartmentDAO.findDepartmentById(departmentId.trim());

        Faculty faculty = new Faculty(name.trim(), email.trim(), phone.trim(), facultyId.trim(), designation.trim(), department, salary, user);
        facultyMap.put(facultyId.trim(), faculty);
        FacultyDAO.saveFaculty(facultyMap);
        UserDAO.saveUser(userMap);

        return true;
    }

    /**
     * Get a faculty member by ID
     */
    public static Faculty getFacultyById(String facultyId) {
        if (facultyId == null || facultyId.trim().isEmpty()) {
            return null;
        }
        return FacultyDAO.findFacultyById(facultyId.trim());
    }

    /**
     * Get all faculty members
     */
    public static List<Faculty> getAllFaculties() {
        Map<String, Faculty> facultyMap = FacultyDAO.getAllFaculties();
        return new ArrayList<>(facultyMap.values());
    }

    /**
     * Delete a faculty member by ID
     */
    public static boolean deleteFaculty(String facultyId) {
        if (facultyId == null || facultyId.trim().isEmpty()) return false;
        Map<String, Faculty> facultyMap = FacultyDAO.getAllFaculties();
        Faculty faculty = facultyMap.remove(facultyId.trim());
        if (faculty != null) {
            FacultyDAO.saveFaculty(facultyMap);
            Map<String, User> userMap = UserDAO.getAllUsers();
            if (faculty.getUser() != null) {
                userMap.remove(faculty.getUser().getUsername());
                UserDAO.saveUser(userMap);
            }
            return true;
        }
        return false;
    }

    /**
     * Search faculty by keyword
     */
    public static List<Faculty> searchFaculty(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return getAllFaculties();
        String lowerKeyword = keyword.trim().toLowerCase();
        List<Faculty> results = new ArrayList<>();
        for (Faculty f : getAllFaculties()) {
            if (f.getName().toLowerCase().contains(lowerKeyword) ||
                f.getFacultyId().toLowerCase().contains(lowerKeyword) ||
                f.getDesignation().toLowerCase().contains(lowerKeyword)) {
                results.add(f);
            }
        }
        return results;
    }

    /**
     * Validate faculty inputs
     */

    // front end // console based
    //manage faculty is only for the admin

    public static void manageFaculty(){
        boolean running = true;
        while(running) {
            System.out.println(TOP);
            System.out.println(center("MANAGE FACULTY"));
            System.out.println(LINE);
            System.out.println(row(""));
            System.out.println(row("   [ 1 ]  View All Faculty"));
            System.out.println(row("   [ 2 ]  Add New Faculty"));
            System.out.println(row("   [ 3 ]  Search Faculty"));
            System.out.println(row("   [ 4 ]  Delete Faculty"));
            System.out.println(row("   [ 5 ]  Back"));
            System.out.println(BOT);
            System.out.println();

            int choice = InputHelper.getChoice();
            switch(choice){
                case 1:
                    viewAllFaculty();
                    break;
                case 2:
                    addNewFaculty();
                    break;
                case 3:
                    searchFacultyConsole();
                    break;
                case 4:
                    deleteFacultyConsole();
                    break;
                case 5:
                    running = false;
                    return;
                default:
                    System.out.println("Invalid Choice.");

            }
        }
    }

    private static void viewAllFaculty(){
        List<Faculty> faculties = FacultyService.getAllFaculties();

        if(faculties.isEmpty()){
            System.out.println("No faculty found." );
            return;
        }

        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                           ALL FACULTY                                ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        for (Faculty f : faculties) {
            System.out.println(f.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private static void searchFacultyConsole() {
        System.out.print("Enter search keyword (Name, ID, Designation): ");
        String keyword = InputHelper.readLine();
        List<Faculty> results = searchFaculty(keyword);
        if (results.isEmpty()) {
            System.out.println("No faculty matched your search.");
            return;
        }
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                          SEARCH RESULTS                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        for (Faculty f : results) {
            System.out.println(f.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private static void deleteFacultyConsole() {
        System.out.print("Enter Faculty ID to delete: ");
        String facultyId = InputHelper.readLine();
        boolean result = deleteFaculty(facultyId);
        if (result) {
            System.out.println("Faculty deleted successfully.");
        } else {
            System.out.println("Failed to delete faculty. ID not found.");
        }
    }
    private static void addNewFaculty(){
        System.out.print("Enter Faculty Name :");
        String name = InputHelper.readLine();
        System.out.print("Enter Faculty's Email :");
        String email = InputHelper.readLine();
        System.out.print("Enter Faculty's Phone number :");
        String phone = InputHelper.readLine();
        System.out.print("Enter Faculty ID :");
        String facultyId = InputHelper.readLine();
        System.out.print("Enter Designation :");
        String designation = InputHelper.readLine();
        System.out.print("Enter Salary :");
        double salary = InputHelper.readDouble();
        System.out.print("Enter Department ID :");
        String departmentId = InputHelper.readLine();
        System.out.print("Enter Faculty's profile User name:");
        String username = InputHelper.readLine();
        System.out.print("Enter Faculty's profile  password:");
        String password = InputHelper.readLine();

        boolean result  = addFaculty(name,email,phone,facultyId,designation,salary,departmentId,username,password);
        if(result){
            System.out.println("Faculty added successfully.");
        }
        else{
            System.out.println("✗ Failed. Check faculty ID is unique and username has Faculty role.");
        }
    }
}
