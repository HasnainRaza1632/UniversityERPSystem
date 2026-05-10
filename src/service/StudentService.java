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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentService {

    /**
     * Register a new student
     */
    public static boolean registerStudent(String name, String email, String phone, String regId, int semester, String username) {
        String validationError = validateStudentInputs(name, email, phone, regId, semester, username);
        if (validationError != null) {
            return false;
        }

        Map<String, Student> studentMap = StudentDAO.getAllStudents();
        
        if (studentMap.containsKey(regId.trim())) {
            return false; // Student ID already taken
        }

        User user = UserDAO.findUserByUsername(username.trim());
        if (user == null || !user.getRole().equalsIgnoreCase("Student")) {
            return false; // User not found or not a student
        }

        Student student = new Student(name.trim(), email.trim(), phone.trim(), regId.trim(), semester, user);
        studentMap.put(regId.trim(), student);
        StudentDAO.saveStudent(studentMap);

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
    private static String validateStudentInputs(String name, String email, String phone, String regId, int semester, String username) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be empty";
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return "Invalid email format";
        }
        if (phone == null || phone.trim().isEmpty() || phone.length() < 12) {
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
        return null;
    }
}
