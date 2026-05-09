package dao;

import model.Course;
import model.Exam;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class ExamDAO {
    private static final String FILE_NAME = "src/filehandling/exams.txt";

    public static Map<String, Exam> getAllExams() {
        Map<String, Exam> examMap = new HashMap<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return examMap;
        }

        Map<String, Course> allCourses = CourseDAO.getAllCourses();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Expecting 8 fields: examId, courseId, examType, examDate, examTime, venue, totalMarks, duration
                if (data.length < 8) continue;

                String examId = data[0];
                String courseId = data[1];
                String examType = data[2];
                LocalDate examDate = LocalDate.parse(data[3]);
                LocalTime examTime = LocalTime.parse(data[4]);
                String venue = data[5];
                int totalMarks = Integer.parseInt(data[6]);
                int duration = Integer.parseInt(data[7]);

                Course course = allCourses.get(courseId);

                Exam exam = new Exam(examId, course, examType, examDate, examTime, venue, totalMarks, duration);
                examMap.put(examId, exam);
            }
        } catch (IOException | DateTimeParseException | NumberFormatException e) {
            System.err.printf("Error reading exams: %s%n", e.getMessage());
        }
        return examMap;
    }

    public static void saveExam(Map<String, Exam> examMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Exam exam : examMap.values()) {
                String courseId = (exam.getCourse() != null) ? exam.getCourse().getCourseId() : "null";
                writer.write(exam.getExamId() + "," + courseId + "," + exam.getExamType() + "," +
                        exam.getExamDate().toString() + "," + exam.getExamTime().toString() + "," +
                        exam.getVenue() + "," + exam.getTotalMarks() + "," + exam.getDuration());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.printf("Error saving exams: %s%n", e.getMessage());
        }
    }
}
