package service;

import dao.CourseDAO;
import dao.ExamDAO;
import model.Course;
import model.Exam;
import utils.InputHelper;

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


    //front end // console based
    // manage exam function only for admin

    public static  void manageExams(){
        boolean running = true;
        while(running){
            System.out.println("========================================\n" +
                    "              MANAGE EXAMS             \n" +
                    "========================================\n" +
                    "1. View All Exams\n" +
                    "2. Schedule New Exam\n" +
                    "3. Back\n" +
                    "========================================");
            int choice =InputHelper.getChoice();
            switch (choice){
                case 1:
                    viewAllExams();
                    break;
                case 2:
                    scheduleNewExam();
                    break;
                case 3:
                    running = false;
                    return;
                default:
                    System.out.println("Invalid choice");

            }
        }
    }
    //manage exam functions
    private static void viewAllExams(){
        List<Exam> exams = ExamService.getAllExams();
        if(exams.isEmpty()){
            System.out.println("No exams scheduled yet.");
            return;
        }
        for (Exam exam : exams) {
            System.out.println(exam.getDetails());
        }
    }
    private static void scheduleNewExam(){
        System.out.print("Enter exam ID :");
        String examId = InputHelper.readLine();

        System.out.print("Enter exam duration :");
        int duration = InputHelper.readInt();

        System.out.print("Enter Total Marks :");
        int totalMarks = InputHelper.readInt();

        System.out.print("Enter exam venue :");
        String venue = InputHelper.readLine();

        System.out.print("Enter exam Time :");
        String time = InputHelper.readLine();

        System.out.print("Enter exam Date :");
        String date = InputHelper.readLine();

        System.out.print("Enter exam Type :");
        String examType = InputHelper.readLine();

        System.out.print("Enter Course ID:");
        String courseId = InputHelper.readLine();

        boolean result = ExamService.scheduleExam(examId, courseId, examType, date, time, venue, totalMarks, duration);
        if(result){
            System.out.println("Exam scheduled successfully");
        } else{
            System.out.println("✗ Failed. Check that Course ID exists, date format is YYYY-MM-DD, time is HH:MM, and marks/duration are positive.");
        }
    }
}
