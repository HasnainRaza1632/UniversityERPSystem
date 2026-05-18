package service;

import dao.DepartmentDAO;
import model.Department;
import utils.InputHelper;
import validation.DepartmentValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DepartmentService {

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
     * Add a new department
     */
    public static boolean addDepartment(String departmentId, String departmentName, String headOfDepartment) {
        String validationError = DepartmentValidator.validate(departmentId, departmentName);
        if (validationError != null) {
            return false;
        }

        Map<String, Department> deptMap = DepartmentDAO.getAllDepartments();
        if (deptMap.containsKey(departmentId.trim())) {
            return false; // Department already exists
        }

        Department department = new Department(departmentId.trim(), departmentName.trim());
        if (headOfDepartment != null && !headOfDepartment.trim().isEmpty()) {
            department.setHeadOfDepartment(headOfDepartment.trim());
        }

        deptMap.put(departmentId.trim(), department);
        DepartmentDAO.saveDepartment(deptMap);

        return true;
    }

    /**
     * Get a department by its ID
     */
    public static Department getDepartmentById(String departmentId) {
        if (departmentId == null || departmentId.trim().isEmpty()) {
            return null;
        }
        return DepartmentDAO.findDepartmentById(departmentId.trim());
    }

    /**
     * Get all departments
     */
    public static List<Department> getAllDepartments() {
        Map<String, Department> deptMap = DepartmentDAO.getAllDepartments();
        return new ArrayList<>(deptMap.values());
    }

    /**
     * Delete a department by ID
     */
    public static boolean deleteDepartment(String departmentId) {
        if (departmentId == null || departmentId.trim().isEmpty()) return false;
        Map<String, Department> deptMap = DepartmentDAO.getAllDepartments();
        if (deptMap.remove(departmentId.trim()) != null) {
            DepartmentDAO.saveDepartment(deptMap);
            return true;
        }
        return false;
    }

    /**
     * Search departments by keyword
     */
    public static List<Department> searchDepartments(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return getAllDepartments();
        String lowerKeyword = keyword.trim().toLowerCase();
        List<Department> results = new ArrayList<>();
        for (Department d : getAllDepartments()) {
            if (d.getDeptName().toLowerCase().contains(lowerKeyword) ||
                d.getDeptId().toLowerCase().contains(lowerKeyword)) {
                results.add(d);
            }
        }
        return results;
    }

    /**
     * Assign a student to a department
     * Defers to StudentService to safely update the Student DAO object
     */
    public static boolean assignStudentToDepartment(String studentId, String departmentId) {
        return StudentService.assignDepartment(studentId, departmentId);
    }

    /**
     * Validate department inputs
     */

    //front end // console based
    // printing all department for the user
    private static void viewAllDepartments() {
        List<Department> departments = DepartmentService.getAllDepartments();
        if (departments.isEmpty()) {
            System.out.println("No departments found.");
            return;
        }
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                         ALL DEPARTMENTS                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        for (Department d : departments) {
            System.out.println(d.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }
    // adding new department
    private static void addNewDepartment(){
        System.out.print("Enter Department Name :");
        String deptName = InputHelper.readLine();
        System.out.print("Enter Department ID:");
        String deptId = InputHelper.readLine();
        System.out.print("Head of the Department :");
        String hod  = InputHelper.readLine();
        boolean result = addDepartment(deptId, deptName, hod);
        if(result){
            System.out.println("✓ Department added successfully.");
        }
        else{
            System.out.println("✗ Failed. ID may already exist.");
        }

    }
    //departments are managed by the admin only
    public static void manageDepartments(){
        boolean running = true;
        while(running){
            System.out.println(TOP);
            System.out.println(center("MANAGE DEPARTMENTS"));
            System.out.println(LINE);
            System.out.println(row(""));
            System.out.println(row("   [ 1 ]  View All Departments"));
            System.out.println(row("   [ 2 ]  Add New Department"));
            System.out.println(row("   [ 3 ]  Search Department"));
            System.out.println(row("   [ 4 ]  Delete Department"));
            System.out.println(row("   [ 5 ]  Back"));
            System.out.println(BOT);
            System.out.println();
            switch(InputHelper.getChoice()){
                case 1:
                    DepartmentService.viewAllDepartments();
                    break;
                case 2:
                    DepartmentService.addNewDepartment();
                    break;
                case 3:
                    DepartmentService.searchDepartmentConsole();
                    break;
                case 4:
                    DepartmentService.deleteDepartmentConsole();
                    break;
                case 5:
                    running = false;
                    return;
                default:
                    System.out.println("Invalid choice.");

            }
        }
    }

    private static void searchDepartmentConsole() {
        System.out.print("Enter search keyword (Department Name, ID): ");
        String keyword = InputHelper.readLine();
        List<Department> results = searchDepartments(keyword);
        if (results.isEmpty()) {
            System.out.println("No departments matched your search.");
            return;
        }
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                          SEARCH RESULTS                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        for (Department d : results) {
            System.out.println(d.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private static void deleteDepartmentConsole() {
        System.out.print("Enter Department ID to delete: ");
        String deptId = InputHelper.readLine();
        boolean result = deleteDepartment(deptId);
        if (result) {
            System.out.println("Department deleted successfully.");
        } else {
            System.out.println("Failed to delete department. ID not found.");
        }
    }
}
