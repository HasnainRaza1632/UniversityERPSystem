package validation;

public class StudentValidator {

    public static String validate(String name, String email, String phone,
                                  String regId, int semester,
                                  String username, String password) {
        if (name == null || name.trim().isEmpty())
            return "Name cannot be empty";
        if (email == null || email.trim().isEmpty() || !email.contains("@"))
            return "Invalid email format";
        if (phone == null || phone.trim().isEmpty() || phone.length() < 11)
            return "Invalid phone number";
        if (regId == null || regId.trim().isEmpty())
            return "Registration ID cannot be empty";
        if (semester < 1 || semester > 8)
            return "Semester must be between 1 and 8";
        if (username == null || username.trim().isEmpty())
            return "Username cannot be empty";
        if (password == null || password.trim().isEmpty())
            return "Password cannot be empty";
        if (password.trim().length() < 6)
            return "Password must be at least 6 characters";
        return null;
    }
}
