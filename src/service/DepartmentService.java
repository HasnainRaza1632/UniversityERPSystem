package service;

import dao.DepartmentDAO;
import model.Department;

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
}
