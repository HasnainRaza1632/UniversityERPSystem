package validation;

public class DepartmentValidator {

    public static String validate(String departmentId, String departmentName) {
        if (departmentId == null || departmentId.trim().isEmpty())
            return "Department ID cannot be empty";
        if (departmentName == null || departmentName.trim().isEmpty())
            return "Department name cannot be empty";
        return null;
    }
}
