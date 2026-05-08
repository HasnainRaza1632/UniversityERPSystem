package model;

public class Grade {
    private Course course;
    private Student student;
    private String letterGrade;
    private double gradePoint;
    private String semester;

    public Grade(Course course , Student student , String letterGrade , double gradePoint , String semester){
        this.course = course;
        this.student = student;
        this.letterGrade = letterGrade;
        this.gradePoint = calculateGradePoint(letterGrade);
        this.semester = semester;
    }

    private double calculateGradePoint(String letterGrade) {
        switch (letterGrade.toUpperCase()) {
            case "A":
                return 4.0;
            case "A-":
                return 3.67;
            case "B+":
                return 3.33;
            case "B":
                return 3.0;
            case "B-":
                return 2.67;
            case "C+":
                return 2.33;
            case "C":
                return 2.0;
            case "C-":
                return 1.67;
            case "D":
                return 1.0;
            case "F":
                return 0.0;
            default:
                return 0.0;
        }
    }

    public Course getCourse() {
        return course;
    }

    public Student getStudent() {
        return student;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public String getSemester() {
        return semester;
    }

    // Setters
    public void setCourse(Course course) {
        this.course = course;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
        this.gradePoint = calculateGradePoint(letterGrade);
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDetails(){
        return String.format("%-10s %-30s %3d credits   Grade: %-3s (%.1f)",
                course.getCourseId(),
                course.getCourseName(),
                course.getCreditHours(),
                letterGrade,
                gradePoint);
    }

    @Override
    public String toString() {
        return course.getCourseId() + "," + letterGrade + "," + semester;
    }

}
