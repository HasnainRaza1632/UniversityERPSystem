package service;

import dao.CourseDAO;
import dao.GradeDAO;
import dao.StudentDAO;
import model.Course;
import model.Grade;
import model.Student;
import utils.InputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GradeService {

    /**
     * Assign a grade to a student for a specific course
     */
    public static boolean assignGrade(String studentId, String courseId, String gradeLetter, double gradePoint) {
        String validationError = validateGradeInputs(studentId, courseId, gradeLetter);
        if (validationError != null) {
            return false;
        }

        Student student = StudentDAO.getStudentOnlyById(studentId.trim());
        if (student == null) {
            return false;
        }

        Map<String, Course> courseMap = CourseDAO.getAllCourses();
        Course course = courseMap.get(courseId.trim());
        if (course == null) {
            return false;
        }

        Map<String, Grade> gradeMap = GradeDAO.getAllGrades();
        String gradeId = studentId.trim() + "_" + courseId.trim();

        // gradePoint is calculated dynamically inside Grade model constructor usually,
        // so it accepts gradeLetter and ignores gradePoint parameter if your design computes it inside.
        Grade grade = new Grade(gradeId, course, student, gradeLetter.trim().toUpperCase(), "N/A");
        gradeMap.put(gradeId, grade);
        GradeDAO.saveGrade(gradeMap);

        return true;
    }

    /**
     * Update an existing grade
     */
    public static boolean updateGrade(String studentId, String courseId, String newGradeLetter) {
        if (studentId == null || courseId == null || newGradeLetter == null) {
            return false;
        }

        Map<String, Grade> gradeMap = GradeDAO.getAllGrades();
        String gradeId = studentId.trim() + "_" + courseId.trim();

        if (!gradeMap.containsKey(gradeId)) {
            return false; // Grade not found
        }

        Grade grade = gradeMap.get(gradeId);
        grade.setLetterGrade(newGradeLetter.trim().toUpperCase());
        GradeDAO.saveGrade(gradeMap);

        return true;
    }

    /**
     * Get all grades for a specific student
     */
    public static List<Grade> getGradesByStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return GradeDAO.getGradesByStudentId(studentId.trim());
    }

    /**
     * Calculate GPA for a student
     * Delegates to StudentService to avoid duplicated logic
     */
    public static double calculateGPA(String studentId) {
        return StudentService.calculateGPA(studentId);
    }

    /**
     * Validate grade inputs
     */
    private static String validateGradeInputs(String studentId, String courseId, String gradeLetter) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return "Student ID cannot be empty";
        }
        if (courseId == null || courseId.trim().isEmpty()) {
            return "Course ID cannot be empty";
        }
        if (gradeLetter == null || gradeLetter.trim().isEmpty()) {
            return "Grade letter cannot be empty";
        }
        
        String upperGrade = gradeLetter.trim().toUpperCase();
        if (!upperGrade.matches("A\\+?|A-|B\\+?|B-|C\\+?|C-|D\\+?|D|F")) {
            return "Invalid grade letter format";
        }
        
        return null;
    }
    //front end // console based
    // only for admin
    public static void assignGradeToStudents(){
        System.out.println("Valid grades: A, A-, B+, B, B-, C+, C, C-, D, F");
        System.out.print("Enter Student ID: ");
        String studentId = InputHelper.readLine();
        System.out.print("Enter Course ID: ");
        String courseId = InputHelper.readLine();
        System.out.print("Enter Grade: ");
        String gradeLetter = InputHelper.readLine();
        boolean result = GradeService.assignGrade(studentId, courseId, gradeLetter, 0);
        if(result){
            System.out.println("Grade Assigned");
        }
        else{
            System.out.println("something went wrong");
        }
    }
}
