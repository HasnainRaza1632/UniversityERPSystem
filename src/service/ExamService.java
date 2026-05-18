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

    /**
     * Delete an exam by ID
     */
    public static boolean deleteExam(String examId) {
        if (examId == null || examId.trim().isEmpty()) return false;
        Map<String, Exam> examMap = ExamDAO.getAllExams();
        if (examMap.remove(examId.trim()) != null) {
            ExamDAO.saveExam(examMap);
            return true;
        }
        return false;
    }

    /**
     * Search exams by keyword
     */
    public static List<Exam> searchExams(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return getAllExams();
        String lowerKeyword = keyword.trim().toLowerCase();
        List<Exam> results = new ArrayList<>();
        for (Exam e : getAllExams()) {
            if (e.getExamId().toLowerCase().contains(lowerKeyword) ||
                (e.getCourse() != null && e.getCourse().getCourseName().toLowerCase().contains(lowerKeyword)) ||
                (e.getCourse() != null && e.getCourse().getCourseId().toLowerCase().contains(lowerKeyword)) ||
                e.getExamType().toLowerCase().contains(lowerKeyword) ||
                e.getVenue().toLowerCase().contains(lowerKeyword)) {
                results.add(e);
            }
        }
        return results;
    }


    //front end // console based
    // manage exam function only for admin

    public static  void manageExams(){
        boolean running = true;
        while(running){
            System.out.println(TOP);
            System.out.println(center("MANAGE EXAMS"));
            System.out.println(LINE);
            System.out.println(row(""));
            System.out.println(row("   [ 1 ]  View All Exams"));
            System.out.println(row("   [ 2 ]  Schedule New Exam"));
            System.out.println(row("   [ 3 ]  Search Exam"));
            System.out.println(row("   [ 4 ]  Delete Exam"));
            System.out.println(row("   [ 5 ]  Back"));
            System.out.println(BOT);
            System.out.println();
            int choice =InputHelper.getChoice();
            switch (choice){
                case 1:
                    viewAllExams();
                    break;
                case 2:
                    scheduleNewExam();
                    break;
                case 3:
                    searchExamConsole();
                    break;
                case 4:
                    deleteExamConsole();
                    break;
                case 5:
                    running = false;
                    return;
                default:
                    System.out.println("Invalid choice");

            }
        }
    }
    //manage exam functions
    public static void viewAllExams(){
        List<Exam> exams = ExamService.getAllExams();
        if(exams.isEmpty()){
            System.out.println("No exams scheduled yet.");
            return;
        }
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                             ALL EXAMS                                ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        for (Exam exam : exams) {
            System.out.println(exam.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private static void searchExamConsole() {
        System.out.print("Enter search keyword (Exam ID, Course, Type, Venue): ");
        String keyword = InputHelper.readLine();
        List<Exam> results = searchExams(keyword);
        if (results.isEmpty()) {
            System.out.println("No exams matched your search.");
            return;
        }
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                          SEARCH RESULTS                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        for (Exam exam : results) {
            System.out.println(exam.getDetails());
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private static void deleteExamConsole() {
        System.out.print("Enter Exam ID to delete: ");
        String examId = InputHelper.readLine();
        boolean result = deleteExam(examId);
        if (result) {
            System.out.println("Exam deleted successfully.");
        } else {
            System.out.println("Failed to delete exam. ID not found.");
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
