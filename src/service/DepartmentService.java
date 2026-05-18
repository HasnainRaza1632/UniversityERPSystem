package service;

import dao.DepartmentDAO;
import model.Department;
import utils.InputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DepartmentService {

    /**
     * Add a new department
     */
    public static boolean addDepartment(String departmentId, String departmentName, String headOfDepartment) {
        String validationError = validateDepartmentInputs(departmentId, departmentName);
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
     * Assign a student to a department
     * Defers to StudentService to safely update the Student DAO object
     */
    public static boolean assignStudentToDepartment(String studentId, String departmentId) {
        return StudentService.assignDepartment(studentId, departmentId);
    }

    /**
     * Validate department inputs
     */
    private static String validateDepartmentInputs(String departmentId, String departmentName) {
        if (departmentId == null || departmentId.trim().isEmpty()) {
            return "Department ID cannot be empty";
        }
        if (departmentName == null || departmentName.trim().isEmpty()) {
            return "Department name cannot be empty";
        }
        return null;
    }
    //front end // console based
    // printing all department for the user
    private static void viewAllDepartments() {
        List<Department> departments = DepartmentService.getAllDepartments();
        if (departments.isEmpty()) {
            System.out.println("No departments found.");
            return;
        }
        System.out.println("-------- ALL DEPARTMENTS --------");
        for (Department d : departments) {
            System.out.println(d.getDetails());
        }
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
            System.out.println("========================================\n" +
                    "         MANAGE DEPARTMENTS            \n" +
                    "========================================\n" +
                    "1. View All Departments\n" +
                    "2. Add New Department\n" +
                    "3. Back\n" +
                    "========================================");
            switch(InputHelper.getChoice()){
                case 1:
                    DepartmentService.viewAllDepartments();
                    break;
                case 2:
                    DepartmentService.addNewDepartment();
                    break;
                case 3:
                    running = false;
                    return;
                default:
                    System.out.println("Invalid choice.");

            }
        }
    }
}
