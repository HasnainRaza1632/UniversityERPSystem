package service;

import dao.CourseDAO;
import dao.ExamDAO;
import model.Course;
import model.Exam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExamService {

    /**
     * Schedule a new exam
     */
    public static boolean scheduleExam(String examId, String courseId, String examType, String dateStr, String timeStr, String venue, int totalMarks, int duration) {
        if (examId == null || examId.trim().isEmpty()) return false;
        
        Map<String, Exam> examMap = ExamDAO.getAllExams();
        if (examMap.containsKey(examId.trim())) {
            return false; // Exam already exists
        }

        Map<String, Course> courseMap = CourseDAO.getAllCourses();
        Course course = courseMap.get(courseId.trim());
        if (course == null) return false;

        LocalDate examDate;
        LocalTime examTime;
        try {
            examDate = LocalDate.parse(dateStr.trim());
            examTime = LocalTime.parse(timeStr.trim());
        } catch (DateTimeParseException e) {
            return false; // Invalid date/time format
        }

        if (totalMarks <= 0 || duration <= 0) return false;

        Exam exam = new Exam(examId.trim(), course, examType.trim(), examDate, examTime, venue.trim(), totalMarks, duration);
        examMap.put(examId.trim(), exam);
        ExamDAO.saveExam(examMap);

        return true;
    }

    /**
     * Get all scheduled exams
     */
    public static List<Exam> getAllExams() {
        Map<String, Exam> examMap = ExamDAO.getAllExams();
        return new ArrayList<>(examMap.values());
    }
}
