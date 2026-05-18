package service;

import dao.DepartmentDAO;
import dao.FacultyDAO;
import dao.UserDAO;
import model.Department;
import model.Faculty;
import model.User;
import utils.InputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FacultyService {

    /**
     * Add a new faculty member
     */
    public static boolean addFaculty(String name, String email, String phone, String facultyId, String designation, double salary, String departmentId, String username,String password) {
        String validationError = validateFacultyInputs(name, email, phone, facultyId, designation, salary, username,password);
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
     * Validate faculty inputs
     */
    private static String validateFacultyInputs(String name, String email, String phone, String facultyId, String designation, double salary, String username,String password) {
        if (name == null || name.trim().isEmpty()) return "Name cannot be empty";
        if (email == null || !email.contains("@")) return "Invalid email format";
        if (phone == null || phone.trim().isEmpty() || phone.length() < 10) return "Invalid phone number";
        if (facultyId == null || facultyId.trim().isEmpty()) return "Faculty ID cannot be empty";
        if (designation == null || designation.trim().isEmpty()) return "Designation cannot be empty";
        if (salary < 0) return "Salary cannot be negative";
        if (username == null || username.trim().isEmpty()) return "Username cannot be empty";
        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty";
        }
        if (password.trim().length() < 6) {
            return "Password must be at least 6 characters";
        }
        return null;
    }
    // front end // console based
    //manage faculty is only for the admin

    public static void manageFaculty(){
        boolean running = true;
        while(running) {
            System.out.println("========================================\n" +
                    "            MANAGE FACULTY             \n" +
                    "========================================\n" +
                    "1. View All Faculty\n" +
                    "2. Add New Faculty\n" +
                    "3. Back\n" +
                    "========================================");

            int choice = InputHelper.getChoice();
            switch(choice){
                case 1:
                    viewAllFaculty();
                    break;
                case 2:
                    addNewFaculty();
                    break;
                case 3:
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

        for (Faculty f : faculties) {
            System.out.println(f.getDetails());
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
