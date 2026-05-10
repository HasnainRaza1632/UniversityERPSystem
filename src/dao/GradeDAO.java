package dao;

import model.Course;
import model.Grade;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeDAO {
    private static final String FILE_NAME = "src/filehandling/grades.txt";

    public static Map<String, Grade> getAllGrades() {
        Map<String, Grade> gradeMap = new HashMap<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return gradeMap;
        }

        Map<String, Course> allCourses = CourseDAO.getAllCourses();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Format: studentId,courseId,gradeLetter,gradePoint
                if (data.length < 4) continue;

                String studentId = data[0];
                String courseId = data[1];
                String gradeLetter = data[2];

                Course course = allCourses.get(courseId);

                // ✅ FIXED: Don't load student here (avoid circular dependency)
                // Student will be set later in StudentDAO

                String gradeId = studentId + "_" + courseId;
                String semester = "N/A";

                Grade grade = new Grade(gradeId, course, null, gradeLetter, semester);
                gradeMap.put(gradeId, grade);
            }
        } catch (IOException e) {
            System.err.printf("Error reading grades: %s%n", e.getMessage());
        }
        return gradeMap;
    }

    public static void saveGrade(Map<String, Grade> gradeMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Grade grade : gradeMap.values()) {
                String studentId = "null";
                if (grade.getStudent() != null) {
                    studentId = grade.getStudent().getRegId();
                } else if (grade.getGradeId() != null && grade.getGradeId().contains("_")) {
                    studentId = grade.getGradeId().split("_")[0];
                }

                String courseId = "null";
                if (grade.getCourse() != null) {
                    courseId = grade.getCourse().getCourseId();
                } else if (grade.getGradeId() != null && grade.getGradeId().contains("_")) {
                    courseId = grade.getGradeId().split("_")[1];
                }

                writer.write(studentId + "," + courseId + "," + grade.getLetterGrade() + "," + grade.getGradePoint());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.printf("Error saving grades: %s%n", e.getMessage());
        }
    }

    public static List<Grade> getGradesByStudentId(String targetStudentId) {
        List<Grade> studentGrades = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return studentGrades;

        Map<String, Course> allCourses = CourseDAO.getAllCourses();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 4) continue;

                String studentId = data[0];
                String courseId = data[1];
                String gradeLetter = data[2];

                if (studentId.equals(targetStudentId)) {
                    Course course = allCourses.get(courseId);

                    String gradeId = studentId + "_" + courseId;
                    Grade grade = new Grade(gradeId, course, null, gradeLetter, "N/A");
                    studentGrades.add(grade);
                }
            }
        } catch (IOException e) {
            System.err.printf("Error reading student grades: %s%n", e.getMessage());
        }
        return studentGrades;
    }
}