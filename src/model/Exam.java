package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Exam {
    private String examId;
    private Course course;
    private String examType;
    private LocalDate examDate;
    private LocalTime examTime;
    private String venue;
    private int totalMarks;
    private int duration;

    public Exam(String examId, Course course, String examType, LocalDate examDate,
                LocalTime examTime, String venue, int totalMarks, int duration) {
        this.examId = examId;
        this.course = course;
        this.examType = examType;
        this.examDate = examDate;
        this.examTime = examTime;
        this.venue = venue;
        this.totalMarks = totalMarks;
        this.duration = duration;
    }

    // Getters
    public String getExamId() {
        return examId;
    }

    public Course getCourse() {
        return course;
    }

    public String getExamType() {
        return examType;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public LocalTime getExamTime() {
        return examTime;
    }

    public String getVenue() {
        return venue;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public int getDuration() {
        return duration;
    }

    // Setters
    public void setExamId(String examId) {
        this.examId = examId;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public void setExamTime(LocalTime examTime) {
        this.examTime = examTime;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }



    public String getDetails() {
        String line = "╠══════════════════════════════════════════════════════════════════════╣";
        String top =  "╔══════════════════════════════════════════════════════════════════════╗";
        String bot =  "╚══════════════════════════════════════════════════════════════════════╝";
        String title = "║                          EXAM DETAILS                               ║";
        return "\n" + top +
                "\n" + title +
                "\n" + line +
                "\n" + formatLine("Exam ID     : ", getExamId()) +
                "\n" + formatLine("Course      : ", getCourse() != null ? getCourse().getCourseName() : "N/A") +
                "\n" + formatLine("Type        : ", getExamType()) +
                "\n" + formatLine("Date        : ", String.valueOf(getExamDate())) +
                "\n" + formatLine("Time        : ", String.valueOf(getExamTime())) +
                "\n" + formatLine("Venue       : ", getVenue()) +
                "\n" + formatLine("Total Marks : ", String.valueOf(getTotalMarks())) +
                "\n" + formatLine("Duration    : ", getDuration() + " mins") +
                "\n" + bot;
    }
    private static String formatLine(String label, String value) {
        String content = label + value;
        int totalWidth = 68;
        int padding = totalWidth - content.length();
        if (padding < 0) {
            content = content.substring(0, totalWidth - 3) + "...";
            padding = 0;
        }
        return "║  " + content + " ".repeat(padding) + "  ║";
    }

    @Override
    public String toString() {
        return examId + "," + course.getCourseId() + "," + examType + "," +
                examDate + "," + examTime + "," + venue + "," + totalMarks + "," + duration;
    }
}
