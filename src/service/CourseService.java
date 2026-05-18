package service;

import dao.CourseDAO;
import dao.DepartmentDAO;
import dao.StudentDAO;
import model.Course;
import model.Department;
import model.Student;
import utils.InputHelper;

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
    //front end // console based

    public static void manageCourses(){
        boolean running = true;

        while(running) {
            System.out.println("========================================\n" +
                    "            MANAGE COURSES             \n" +
                    "========================================\n" +
                    "1. View All Courses\n" +
                    "2. Add New Course\n" +
                    "3. Back\n" +
                    "========================================");

            int choice = InputHelper.getChoice();
            switch(choice){
                case 1:
                    viewAllCourse();
                    break;
                case 2:
                    addNewCourse();
                    break;
                case 3:
                    running = false;
                    return;
                default:
                    System.out.println("invalid choice");
            }
        }

    }
    private static void viewAllCourse(){
        List<Course> courses = CourseService.getAllCourses();
        if(courses.isEmpty()){
            System.out.println("No courses found");
            return;
        }
        for (Course c : courses) {
            System.out.println(c.getDetails());
        }
    }
    private static void addNewCourse(){
        System.out.print("Enter Department ID:");
        String departmentId = InputHelper.readLine();
        System.out.print("Enter Course ID:");
        String courseId = InputHelper.readLine();
        System.out.print("Enter Course Name:");
        String courseName = InputHelper.readLine();
        System.out.print("Enter Credit Hours:");
        int creditHours = InputHelper.readInt();
        System.out.print("Enter Course Description:");
        String description = InputHelper.readLine();

        boolean added = addCourse(courseId,courseName,creditHours,description,departmentId);
        if(added){
            System.out.println("Course Added successfully");
        }
        else{
            System.out.println("failed to add the course");
        }
    }
}
