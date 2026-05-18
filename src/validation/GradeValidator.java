package validation;

public class GradeValidator {

    public static String validate(String studentId, String courseId, String gradeLetter) {
        if (studentId == null || studentId.trim().isEmpty())
            return "Student ID cannot be empty";
        if (courseId == null || courseId.trim().isEmpty())
            return "Course ID cannot be empty";
        if (gradeLetter == null || gradeLetter.trim().isEmpty())
            return "Grade letter cannot be empty";
        String upper = gradeLetter.trim().toUpperCase();
        if (!upper.matches("A\\+?|A-|B\\+?|B-|C\\+?|C-|D\\+?|D|F"))
            return "Invalid grade letter format";
        return null;
    }
}
