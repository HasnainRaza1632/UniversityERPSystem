package service;

import dao.CourseDAO;
import dao.GradeDAO;
import dao.StudentDAO;
import model.Course;
import model.Grade;
import model.Student;
import utils.InputHelper;
import validation.GradeValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GradeService {

    /**
     * Assign a grade to a student for a specific course
     */
    public static boolean assignGrade(String studentId, String courseId, String gradeLetter, double gradePoint) {
        String validationError = GradeValidator.validate(studentId, courseId, gradeLetter);
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

    // ─── Console UI methods ───────────────────────────────────────────────

    /** Assign a grade to a student (used by Faculty). */
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
            System.out.println("✓ Grade assigned successfully.");
        } else {
            System.out.println("✗ Failed. Check Student ID, Course ID, or grade format.");
        }
    }

    /** View all grades in the system (used by Admin for oversight). */
    public static void viewAllGrades(){
        Map<String, Grade> gradeMap = GradeDAO.getAllGrades();
        if(gradeMap.isEmpty()){
            System.out.println("No grades recorded yet.");
            return;
        }
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                            ALL GRADES                                ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        for(Grade grade : gradeMap.values()){
            System.out.println(grade.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    /** Update an existing grade (used by Admin to correct mistakes). */
    public static void updateGradeConsole(){
        System.out.println("Valid grades: A, A-, B+, B, B-, C+, C, C-, D, F");
        System.out.print("Enter Student ID: ");
        String studentId = InputHelper.readLine();
        System.out.print("Enter Course ID: ");
        String courseId = InputHelper.readLine();
        System.out.print("Enter New Grade: ");
        String newGrade = InputHelper.readLine();
        boolean result = GradeService.updateGrade(studentId, courseId, newGrade);
        if(result){
            System.out.println("✓ Grade updated successfully.");
        } else {
            System.out.println("✗ Failed. No existing grade found for this Student ID + Course ID.");
        }
    }
}
