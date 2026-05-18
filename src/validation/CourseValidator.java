package validation;

public class CourseValidator {

    public static String validate(String courseId, String courseName,
                                  int creditHours, String description,
                                  String departmentId) {
        if (courseId == null || courseId.trim().isEmpty())
            return "Course ID cannot be empty";
        if (courseName == null || courseName.trim().isEmpty())
            return "Course name cannot be empty";
        if (creditHours <= 0)
            return "Credit hours must be greater than 0";
        if (description == null || description.trim().isEmpty())
            return "Description cannot be empty";
        if (departmentId == null || departmentId.trim().isEmpty())
            return "Department ID cannot be empty";
        return null;
    }
}
