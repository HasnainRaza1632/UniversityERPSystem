package service;

import dao.CourseDAO;
import dao.DepartmentDAO;
import dao.StudentDAO;
import model.Course;
import model.Department;
import model.Student;
import utils.InputHelper;
import validation.CourseValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseService {

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
     * Add a new course
     */
    public static boolean addCourse(String courseId, String courseName, int creditHours, String description, String departmentId) {
        String validationError = CourseValidator.validate(courseId, courseName, creditHours, description, departmentId);
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
     * Delete a course by ID
     */
    public static boolean deleteCourse(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) return false;
        Map<String, Course> courseMap = CourseDAO.getAllCourses();
        if (courseMap.remove(courseId.trim()) != null) {
            CourseDAO.saveCourse(courseMap);
            return true;
        }
        return false;
    }

    /**
     * Search courses by keyword
     */
    public static List<Course> searchCourses(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return getAllCourses();
        String lowerKeyword = keyword.trim().toLowerCase();
        List<Course> results = new ArrayList<>();
        for (Course c : getAllCourses()) {
            if (c.getCourseName().toLowerCase().contains(lowerKeyword) ||
                c.getCourseId().toLowerCase().contains(lowerKeyword)) {
                results.add(c);
            }
        }
        return results;
    }

    /**
     * Validate course inputs
     */

    //front end // console based

    public static void manageCourses(){
        boolean running = true;

        while(running) {
            System.out.println(TOP);
            System.out.println(center("MANAGE COURSES"));
            System.out.println(LINE);
            System.out.println(row(""));
            System.out.println(row("   [ 1 ]  View All Courses"));
            System.out.println(row("   [ 2 ]  Add New Course"));
            System.out.println(row("   [ 3 ]  Search Course"));
            System.out.println(row("   [ 4 ]  Delete Course"));
            System.out.println(row("   [ 5 ]  Back"));
            System.out.println(BOT);
            System.out.println();

            int choice = InputHelper.getChoice();
            switch(choice){
                case 1:
                    viewAllCourse();
                    break;
                case 2:
                    addNewCourse();
                    break;
                case 3:
                    searchCourseConsole();
                    break;
                case 4:
                    deleteCourseConsole();
                    break;
                case 5:
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
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                           ALL COURSES                                ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        for (Course c : courses) {
            System.out.println(c.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private static void searchCourseConsole() {
        System.out.print("Enter search keyword (Course Name, ID): ");
        String keyword = InputHelper.readLine();
        List<Course> results = searchCourses(keyword);
        if (results.isEmpty()) {
            System.out.println("No courses matched your search.");
            return;
        }
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                          SEARCH RESULTS                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        for (Course c : results) {
            System.out.println(c.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private static void deleteCourseConsole() {
        System.out.print("Enter Course ID to delete: ");
        String courseId = InputHelper.readLine();
        boolean result = deleteCourse(courseId);
        if (result) {
            System.out.println("Course deleted successfully.");
        } else {
            System.out.println("Failed to delete course. ID not found.");
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
