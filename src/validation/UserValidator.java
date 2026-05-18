package validation;

public class UserValidator {

    public static String validateSignupInputs(String username, String password, String role) {
        if (username == null || username.trim().isEmpty())
            return "Username cannot be empty";
        username = username.trim();
        if (username.length() < 3)
            return "Username must be at least 3 characters";
        if (username.length() > 20)
            return "Username must be less than 20 characters";
        if (username.contains(","))
            return "Username cannot contain commas";
        if (!username.matches("^[a-zA-Z0-9_]+$"))
            return "Username can only contain letters, numbers, and underscores";

        if (password == null || password.trim().isEmpty())
            return "Password cannot be empty";
        password = password.trim();
        if (password.length() < 6)
            return "Password must be at least 6 characters";
        if (password.length() > 30)
            return "Password must be less than 30 characters";
        if (password.contains(","))
            return "Password cannot contain commas";

        if (role == null || role.trim().isEmpty())
            return "Role cannot be empty";
        if (normalizeRole(role.trim()) == null)
            return "Role must be Admin, Student, or Faculty";

        return null;
    }

    public static String validateLoginInputs(String username, String password, String role) {
        if (username == null || username.trim().isEmpty())
            return "Username cannot be empty";
        if (password == null || password.trim().isEmpty())
            return "Password cannot be empty";
        if (role == null || role.trim().isEmpty())
            return "Role cannot be empty";
        if (normalizeRole(role) == null)
            return "Role must be Admin, Student, or Faculty";
        return null;
    }

    public static String normalizeRole(String role) {
        if (role == null || role.trim().isEmpty()) return null;
        role = role.trim();
        if (role.equalsIgnoreCase("Admin"))   return "Admin";
        if (role.equalsIgnoreCase("Student")) return "Student";
        if (role.equalsIgnoreCase("Faculty")) return "Faculty";
        return null;
    }
}
