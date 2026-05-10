package service;

import dao.CourseDAO;
import dao.DepartmentDAO;
import dao.StudentDAO;
import model.Course;
import model.Department;
import model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseService {

    /**
     * Add a new course
     */
    public static boolean addCourse(String courseId, String courseName, int creditHours, String description, String departmentId) {
        String validationError = validateCourseInputs(courseId, courseName, creditHours, description, departmentId);
        if (validationError != null) {
            return false;
        }

        Map<String, Course> courseMap = CourseDAO.getAllCourses();
        if (courseMap.containsKey(courseId.trim())) {
            return false; // Course ID already exists
        }

        Department department = DepartmentDAO.findDepartmentById(departmentId.trim());
        if (department == null) {
            return false; // Department doesn't exist
        }

        Course course = new Course(courseId.trim(), courseName.trim(), creditHours, description.trim(), null, department);
        courseMap.put(courseId.trim(), course);
        CourseDAO.saveCourse(courseMap);

        return true;
    }

    /**
     * Assign a course to a student
     */
    public static boolean assignCourseToStudent(String studentId, String courseId) {
        if (studentId == null || courseId == null) {
            return false;
        }

        Student student = StudentDAO.getStudentOnlyById(studentId.trim());
        if (student == null) {
            return false;
        }

        Map<String, Course> courseMap = CourseDAO.getAllCourses();
        if (!courseMap.containsKey(courseId.trim())) {
            return false;
        }

        // Writes to junction table correctly following architecture spec using DAO
        return CourseDAO.enrollStudentInCourse(studentId.trim(), courseId.trim());
    }

    /**
     * Get a course by its ID
     */
    public static Course getCourseById(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            return null;
        }
        Map<String, Course> courseMap = CourseDAO.getAllCourses();
        return courseMap.get(courseId.trim());
    }

    /**
     * Get courses by department ID
     */
    public static List<Course> getCoursesByDepartment(String departmentId) {
        if (departmentId == null || departmentId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return CourseDAO.getCoursesByDepartmentId(departmentId.trim());
    }

    /**
     * Get all courses
     */
    public static List<Course> getAllCourses() {
        Map<String, Course> courseMap = CourseDAO.getAllCourses();
        return new ArrayList<>(courseMap.values());
    }

    /**
     * Validate course inputs
     */
    private static String validateCourseInputs(String courseId, String courseName, int creditHours, String description, String departmentId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            return "Course ID cannot be empty";
        }
        if (courseName == null || courseName.trim().isEmpty()) {
            return "Course name cannot be empty";
        }
        if (creditHours <= 0) {
            return "Credit hours must be greater than 0";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Description cannot be empty";
        }
        if (departmentId == null || departmentId.trim().isEmpty()) {
            return "Department ID cannot be empty";
        }
        return null;
    }
}
