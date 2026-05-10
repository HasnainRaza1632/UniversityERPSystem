package service;

import dao.DepartmentDAO;
import dao.FacultyDAO;
import dao.UserDAO;
import model.Department;
import model.Faculty;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FacultyService {

    /**
     * Add a new faculty member
     */
    public static boolean addFaculty(String name, String email, String phone, String facultyId, String designation, double salary, String departmentId, String username) {
        String validationError = validateFacultyInputs(name, email, phone, facultyId, designation, salary, username);
        if (validationError != null) {
            return false;
        }

        Map<String, Faculty> facultyMap = FacultyDAO.getAllFaculties();
        if (facultyMap.containsKey(facultyId.trim())) {
            return false; // Faculty ID already exists
        }

        User user = UserDAO.findUserByUsername(username.trim());
        if (user == null || !user.getRole().equalsIgnoreCase("Faculty")) {
            return false; // User not found or not a Faculty
        }

        Department department = DepartmentDAO.findDepartmentById(departmentId.trim());

        Faculty faculty = new Faculty(name.trim(), email.trim(), phone.trim(), facultyId.trim(), designation.trim(), department, salary, user);
        facultyMap.put(facultyId.trim(), faculty);
        FacultyDAO.saveFaculty(facultyMap);

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
    private static String validateFacultyInputs(String name, String email, String phone, String facultyId, String designation, double salary, String username) {
        if (name == null || name.trim().isEmpty()) return "Name cannot be empty";
        if (email == null || !email.contains("@")) return "Invalid email format";
        if (phone == null || phone.trim().isEmpty() || phone.length() < 10) return "Invalid phone number";
        if (facultyId == null || facultyId.trim().isEmpty()) return "Faculty ID cannot be empty";
        if (designation == null || designation.trim().isEmpty()) return "Designation cannot be empty";
        if (salary < 0) return "Salary cannot be negative";
        if (username == null || username.trim().isEmpty()) return "Username cannot be empty";
        
        return null;
    }
}
