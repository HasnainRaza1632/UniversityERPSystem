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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentService {

    /**
     * Register a new student
     */
    public static boolean registerStudent(String name, String email, String phone, String regId, int semester, String username,String password) {
        String validationError = validateStudentInputs(name, email, phone, regId, semester, username,password);
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
        transcript.append("Transcript for: ").append(student.getName())
                  .append(" (").append(student.getRegId()).append(")\n");
        
        if (student.getDepartment() != null) {
            transcript.append("Department: ").append(student.getDepartment().getDeptName()).append("\n");
        }
        
        transcript.append("--------------------------------------------------\n");

        List<Grade> grades = student.getGradesList();
        if (grades == null || grades.isEmpty()) {
            transcript.append("No grades available.\n");
        } else {
            for (Grade grade : grades) {
                Course course = grade.getCourse();
                if (course != null) {
                    transcript.append(String.format("Course: %-20s | Credits: %d | Grade: %-2s | Points: %.1f%n",
                            course.getCourseName(), course.getCreditHours(), grade.getLetterGrade(), grade.getGradePoint()));
                }
            }
        }
        
        transcript.append("--------------------------------------------------\n");
        transcript.append(String.format("Cumulative GPA: %.2f%n", calculateGPA(studentId)));

        return transcript.toString();
    }

    /**
     * Validate student registration inputs
     */
    private static String validateStudentInputs(String name, String email, String phone, String regId, int semester, String username,String password) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be empty";
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return "Invalid email format";
        }
        if (phone == null || phone.trim().isEmpty() || phone.length() < 11) {
            return "Invalid phone number";
        }
        if (regId == null || regId.trim().isEmpty()) {
            return "Registration ID cannot be empty";
        }
        if (semester < 1 || semester > 8) {
            return "Semester must be between 1 and 8";
        }
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty";
        }
        if (password.trim().length() < 6) {
            return "Password must be at least 6 characters";
        }
        return null;
    }

    public static void manageStudents(){
        boolean running = true;
        while(running){
            System.out.println("========================================\n" +
                    "            MANAGE STUDENTS           \n" +
                    "========================================\n" +
                    "1. View All Students\n" +
                    "2. Register New Student\n" +
                    "3. Assign Student to Department\n" +
                    "4. Enroll Student in Course\n" +
                    "5. Back\n" +
                    "========================================");

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
        System.out.println("-------- ALL STUDENTS --------");
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        for (Student student : students) {
            System.out.println(student.getDetails());
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
        String error = validateStudentInputs(name,email,phone,regId,semester,username,password);
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
