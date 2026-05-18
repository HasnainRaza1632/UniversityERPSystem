package validation;

public class FacultyValidator {

    public static String validate(String name, String email, String phone,
                                  String facultyId, String designation,
                                  double salary, String username, String password) {
        if (name == null || name.trim().isEmpty())
            return "Name cannot be empty";
        if (email == null || !email.contains("@"))
            return "Invalid email format";
        if (phone == null || phone.trim().isEmpty() || phone.length() < 10)
            return "Invalid phone number";
        if (facultyId == null || facultyId.trim().isEmpty())
            return "Faculty ID cannot be empty";
        if (designation == null || designation.trim().isEmpty())
            return "Designation cannot be empty";
        if (salary < 0)
            return "Salary cannot be negative";
        if (username == null || username.trim().isEmpty())
            return "Username cannot be empty";
        if (password == null || password.trim().isEmpty())
            return "Password cannot be empty";
        if (password.trim().length() < 6)
            return "Password must be at least 6 characters";
        return null;
    }
}
