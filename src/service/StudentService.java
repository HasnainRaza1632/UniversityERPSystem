package service;

import dao.CourseDAO;
import dao.DepartmentDAO;
import dao.GradeDAO;
import dao.StudentDAO;
import dao.UserDAO;
import model.Course;
import model.Department;
import model.Grade;
import model.Student;
import model.User;
import utils.InputHelper;
import validation.StudentValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentService {

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
     * Register a new student
     */
    public static boolean registerStudent(String name, String email, String phone, String regId, int semester, String username,String password) {
        String validationError = StudentValidator.validate(name, email, phone, regId, semester, username, password);
        if (validationError != null) {
            return false;
        }

        Map<String, Student> studentMap = StudentDAO.getAllStudents();
        Map<String,User> userMap = UserDAO.getAllUsers();

        if (studentMap.containsKey(regId.trim())) {
            return false; // Student ID already taken
        }

        User user = new User(username.trim().toLowerCase(), password.trim(), "Student");
        userMap.put(username.trim().toLowerCase(),user);

        Student student = new Student(name.trim(), email.trim(), phone.trim(), regId.trim(), semester, user);
        studentMap.put(regId.trim(), student);
        StudentDAO.saveStudent(studentMap);
        UserDAO.saveUser(userMap);

        return true;
    }

    /**
     * Assign a student to a department
     */
    public static boolean assignDepartment(String studentId, String departmentId) {
        if (studentId == null || departmentId == null) {
            return false;
        }

        Map<String, Student> studentMap = StudentDAO.getAllStudents();
        Student student = studentMap.get(studentId.trim());
        
        if (student == null) {
            return false;
        }

        Department department = DepartmentDAO.findDepartmentById(departmentId.trim());
        if (department == null) {
            return false;
        }

        student.setDepartment(department);
        StudentDAO.saveStudent(studentMap);

        return true;
    }

    /**
     * Enroll a student in a course
     * Note: Appends directly to student_courses.txt (junction table)
     */
    public static boolean enrollCourse(String studentId, String courseId) {
        if (studentId == null || courseId == null) {
            return false;
        }

        Student student = getStudentById(studentId.trim());
        if (student == null) {
            return false; // Student not found
        }

        Map<String, Course> courseMap = CourseDAO.getAllCourses();
        if (!courseMap.containsKey(courseId.trim())) {
            return false; // Course not found
        }

        return CourseDAO.enrollStudentInCourse(studentId.trim(), courseId.trim());
    }

    /**
     * Get a student by their ID
     */
    public static Student getStudentById(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }

        Map<String, Student> studentMap = StudentDAO.getAllStudents();
        return studentMap.get(studentId.trim());
    }

    /**
     * Get all students
     */
    public static List<Student> getAllStudents() {
        Map<String, Student> studentMap = StudentDAO.getAllStudents();
        return new ArrayList<>(studentMap.values());
    }

    /**
     * Delete a student by ID
     */
    public static boolean deleteStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) return false;
        Map<String, Student> studentMap = StudentDAO.getAllStudents();
        Student student = studentMap.remove(studentId.trim());
        if (student != null) {
            StudentDAO.saveStudent(studentMap);
            Map<String, User> userMap = UserDAO.getAllUsers();
            if (student.getUser() != null) {
                userMap.remove(student.getUser().getUsername());
                UserDAO.saveUser(userMap);
            }
            return true;
        }
        return false;
    }

    /**
     * Search students by keyword (matches name, regId, username)
     */
    public static List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return getAllStudents();
        String lowerKeyword = keyword.trim().toLowerCase();
        List<Student> results = new ArrayList<>();
        for (Student s : getAllStudents()) {
            if (s.getName().toLowerCase().contains(lowerKeyword) ||
                s.getRegId().toLowerCase().contains(lowerKeyword) ||
                (s.getUser() != null && s.getUser().getUsername().toLowerCase().contains(lowerKeyword))) {
                results.add(s);
            }
        }
        return results;
    }

    /**
     * Calculate GPA for a student
     */
    public static double calculateGPA(String studentId) {
        Student student = getStudentById(studentId);
        if (student == null || student.getGradesList() == null || student.getGradesList().isEmpty()) {
            return 0.0;
        }

        double totalGradePoints = 0;
        int totalCreditHours = 0;

        for (Grade grade : student.getGradesList()) {
            Course course = grade.getCourse();
            if (course != null) {
                totalGradePoints += (grade.getGradePoint() * course.getCreditHours());
                totalCreditHours += course.getCreditHours();
            }
        }

        if (totalCreditHours == 0) {
            return 0.0;
        }

        return totalGradePoints / totalCreditHours;
    }

    /**
     * Get transcript for a student (returns a formatted string or null)
     */
    public static String viewTranscript(String studentId) {
        Student student = getStudentById(studentId);
        if (student == null) {
            return null;
        }

        StringBuilder transcript = new StringBuilder();
        transcript.append("╠══════════════════════════════════════════════════════════════════════╣\n");
        transcript.append("║                          STUDENT TRANSCRIPT                          ║\n");
        transcript.append("╠══════════════════════════════════════════════════════════════════════╣\n");
        
        String nameLine = "Transcript for: " + student.getName() + " (" + student.getRegId() + ")";
        int paddingName = 70 - nameLine.length() - 2;
        transcript.append("║ ").append(nameLine).append(" ".repeat(Math.max(0, paddingName))).append(" ║\n");
        
        if (student.getDepartment() != null) {
            String deptLine = "Department: " + student.getDepartment().getDeptName();
            int paddingDept = 70 - deptLine.length() - 2;
            transcript.append("║ ").append(deptLine).append(" ".repeat(Math.max(0, paddingDept))).append(" ║\n");
        }
        
        transcript.append("╠══════════════════════════════════════════════════════════════════════╣\n");

        List<Grade> grades = student.getGradesList();
        if (grades == null || grades.isEmpty()) {
            transcript.append("║ No grades available.                                                 ║\n");
        } else {
            for (Grade grade : grades) {
                Course course = grade.getCourse();
                if (course != null) {
                    String line = String.format("Course: %-20s | Credits: %d | Grade: %-2s | Points: %.1f",
                            course.getCourseName(), course.getCreditHours(), grade.getLetterGrade(), grade.getGradePoint());
                    int paddingLine = 70 - line.length() - 2;
                    transcript.append("║ ").append(line).append(" ".repeat(Math.max(0, paddingLine))).append(" ║\n");
                }
            }
        }
        
        transcript.append("╠══════════════════════════════════════════════════════════════════════╣\n");
        String gpaLine = String.format("Cumulative GPA: %.2f", calculateGPA(studentId));
        int paddingGpa = 70 - gpaLine.length() - 2;
        transcript.append("║ ").append(gpaLine).append(" ".repeat(Math.max(0, paddingGpa))).append(" ║\n");
        transcript.append("╚══════════════════════════════════════════════════════════════════════╝\n");

        return transcript.toString();
    }

    /**
     * Validate student registration inputs
     */


    public static void manageStudents(){
        boolean running = true;
        while(running){
            System.out.println(TOP);
            System.out.println(center("MANAGE STUDENTS"));
            System.out.println(LINE);
            System.out.println(row(""));
            System.out.println(row("   [ 1 ]  View All Students"));
            System.out.println(row("   [ 2 ]  Register New Student"));
            System.out.println(row("   [ 3 ]  Assign Student to Department"));
            System.out.println(row("   [ 4 ]  Enroll Student in Course"));
            System.out.println(row("   [ 5 ]  Search Student"));
            System.out.println(row("   [ 6 ]  Delete Student"));
            System.out.println(row("   [ 7 ]  Back"));
            System.out.println(BOT);
            System.out.println();

            int choice = InputHelper.getChoice();

            switch(choice){
                case 1:
                    viewAllStudents();
                    break;
                case 2:
                    registerNewStudent();
                    break;
                case 3:
                    assignStudentToDepartment();
                    break;
                case 4:
                    enrollStudentInCourse();
                    break;
                case 5:
                    searchStudentConsole();
                    break;
                case 6:
                    deleteStudentConsole();
                    break;
                case 7:
                    running = false;
                    return;
                default:
                    System.out.println("Invalid choice");

            }
        }
    }
    // manageStudents functions
    private static void viewAllStudents(){
        List<Student> students = StudentService.getAllStudents();
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                           ALL STUDENTS                               ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        for (Student student : students) {
            System.out.println(student.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private static void searchStudentConsole() {
        System.out.print("Enter search keyword (Name, ID, Username): ");
        String keyword = InputHelper.readLine();
        List<Student> results = searchStudents(keyword);
        if (results.isEmpty()) {
            System.out.println("No students matched your search.");
            return;
        }
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                          SEARCH RESULTS                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        for (Student student : results) {
            System.out.println(student.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private static void deleteStudentConsole() {
        System.out.print("Enter Registration ID of Student to delete: ");
        String regId = InputHelper.readLine();
        boolean result = deleteStudent(regId);
        if (result) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Failed to delete student. ID not found.");
        }
    }
    private static void registerNewStudent(){
        System.out.print("Enter Student name :");
        String name = InputHelper.readLine();
        System.out.print("Enter Student's email :");
        String email = InputHelper.readLine();
        System.out.print("Enter Student's phone number:");
        String phone = InputHelper.readLine();
        System.out.print("Enter Student's Registration ID:");
        String regId= InputHelper.readLine();
        System.out.print("Enter Semester:");
        int semester = InputHelper.readInt();
        System.out.print("Enter Student's user name:");
        String username = InputHelper.readLine();
        System.out.print("Enter Student's profile password:");
        String password = InputHelper.readLine();
        String error = StudentValidator.validate(name, email, phone, regId, semester, username, password);
        if(error != null){
            System.out.println("Registration failed : "+error);
            return;
        }

        boolean registered = registerStudent(name,email,phone,regId,semester,username,password);
        if(registered){
            System.out.println("Student Registered successfully");
        }
        else{
            System.out.println("✗ Failed. Check that username exists and has Student role, phone is 11 digits, semester is 1-8.");

        }
    }
    private static void assignStudentToDepartment(){
        System.out.print("Enter Student ID: ");
        String studentId = InputHelper.readLine();
        System.out.print("Enter Department ID: ");
        String departmentId = InputHelper.readLine();
        boolean result = StudentService.assignDepartment(studentId, departmentId);
        if(result){
            System.out.println("Assigned");
        }
        else{
            System.out.println("✗ Failed. Student ID or Department ID not found.");
        }

    }
    private static void enrollStudentInCourse(){
        System.out.print("Enter Student ID: ");
        String studentId = InputHelper.readLine();
        System.out.print("Enter Course ID: ");
        String courseId = InputHelper.readLine();

        boolean result = StudentService.enrollCourse(studentId, courseId);
        if(result){
            System.out.println("Enrolled");
        }
        else{
            System.out.println("✗ Failed. Student ID or Course ID not found.");
        }
    }

}
