package service;

import model.User;
import dao.UserDAO;
import java.util.Map;

public class AuthService {

    /**
     * Public signup - Only for Student and Faculty
     * Admin accounts can only be created by existing admins
     */
    public static boolean signUp(String username, String password, String role) {
        // Validate inputs
        String validationError = validateSignupInputs(username, password, role);
        if (validationError != null) {
            return false;
        }

        // Trim inputs
        username = username.trim();
        password = password.trim();
        role = role.trim();

        // 🔒 SECURITY: Block admin signup from public registration
        if (role.equalsIgnoreCase("Admin")) {
            return false; // Admin accounts can only be created by admins
        }

        // Load fresh user data
        Map<String, User> userMap = UserDAO.getAllUsers();

        // Check if user already exists
        if (userMap.containsKey(username)) {
            return false;
        }

        // Normalize role
        role = normalizeRole(role);
        if (role == null || role.equals("Admin")) {
            return false; // Double-check no admin
        }

        // Create and save user
        userMap.put(username, new User(username, password, role));
        UserDAO.saveUser(userMap);

        return true;
    }

    /**
     * Admin-only signup - Can create any type of account
     * Requires admin authentication
     */
    public static boolean adminCreateUser(User admin, String username, String password, String role) {
        // 🔒 SECURITY: Verify the creator is actually an admin
        if (admin == null || !admin.getRole().equalsIgnoreCase("Admin")) {
            return false; // Only admins can create users
        }

        // Validate inputs
        String validationError = validateSignupInputs(username, password, role);
        if (validationError != null) {
            return false;
        }

        // Load fresh user data
        Map<String, User> userMap = UserDAO.getAllUsers();

        // Trim inputs
        username = username.trim();
        password = password.trim();
        role = role.trim();

        // Check if user already exists
        if (userMap.containsKey(username)) {
            return false;
        }

        // Normalize role (admin can create any role including Admin)
        role = normalizeRole(role);
        if (role == null) {
            return false;
        }

        // Create and save user
        userMap.put(username, new User(username, password, role));
        UserDAO.saveUser(userMap);

        return true;
    }

    /**
     * Login method (unchanged)
     */
    public static User login(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        if (role == null || role.trim().isEmpty()) {
            return null;
        }

        Map<String, User> userMap = UserDAO.getAllUsers();

        username = username.trim();
        password = password.trim();
        role = role.trim();

        if (!userMap.containsKey(username)) {
            return null;
        }

        User user = userMap.get(username);

        if (!user.getPassword().equals(password)) {
            return null;
        }

        if (!user.getRole().equalsIgnoreCase(role)) {
            return null;
        }

        return user;
    }

    /**
     * Validate signup inputs
     */
    public static String validateSignupInputs(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty";
        }
        username = username.trim();

        if (username.length() < 3) {
            return "Username must be at least 3 characters";
        }
        if (username.length() > 20) {
            return "Username must be less than 20 characters";
        }
        if (username.contains(",")) {
            return "Username cannot contain commas";
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return "Username can only contain letters, numbers, and underscores";
        }

        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty";
        }
        password = password.trim();

        if (password.length() < 6) {
            return "Password must be at least 6 characters";
        }
        if (password.length() > 30) {
            return "Password must be less than 30 characters";
        }
        if (password.contains(",")) {
            return "Password cannot contain commas";
        }

        if (role == null || role.trim().isEmpty()) {
            return "Role cannot be empty";
        }
        String normalizedRole = normalizeRole(role.trim());
        if (normalizedRole == null) {
            return "Role must be Admin, Student, or Faculty";
        }

        return null;
    }

    /**
     * Check if username already exists
     */
    public static boolean isUsernameTaken(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        Map<String, User> userMap = UserDAO.getAllUsers();
        return userMap.containsKey(username.trim());
    }

    /**
     * Normalize role to standard format
     */
    public static String normalizeRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return null;
        }

        role = role.trim();

        if (role.equalsIgnoreCase("Admin")) {
            return "Admin";
        } else if (role.equalsIgnoreCase("Student")) {
            return "Student";
        } else if (role.equalsIgnoreCase("Faculty")) {
            return "Faculty";
        }

        return null;
    }

    /**
     * Validate login inputs
     */
    public static String validateLoginInputs(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty";
        }
        if (role == null || role.trim().isEmpty()) {
            return "Role cannot be empty";
        }

        String normalizedRole = normalizeRole(role);
        if (normalizedRole == null) {
            return "Role must be Admin, Student, or Faculty";
        }

        return null;
    }

    /**
     * Get user by username
     */
    public static User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        Map<String, User> userMap = UserDAO.getAllUsers();
        return userMap.get(username.trim());
    }

    /**
     * Change user password
     */
    public static boolean changePassword(String username, String oldPassword, String newPassword) {
        if (username == null || oldPassword == null || newPassword == null) {
            return false;
        }

        Map<String, User> userMap = UserDAO.getAllUsers();

        if (!userMap.containsKey(username)) {
            return false;
        }

        User user = userMap.get(username);

        if (!user.getPassword().equals(oldPassword)) {
            return false;
        }

        if (newPassword.trim().length() < 6) {
            return false;
        }

        user.setPassword(newPassword.trim());
        UserDAO.saveUser(userMap);

        return true;
    }

    /**
     * Delete user account (admin-only operation)
     */
    public static boolean deleteUser(User admin, String usernameToDelete) {
        // 🔒 SECURITY: Only admins can delete users
        if (admin == null || !admin.getRole().equalsIgnoreCase("Admin")) {
            return false;
        }

        if (usernameToDelete == null || usernameToDelete.trim().isEmpty()) {
            return false;
        }

        Map<String, User> userMap = UserDAO.getAllUsers();

        if (!userMap.containsKey(usernameToDelete)) {
            return false;
        }

        // 🔒 SECURITY: Prevent admin from deleting themselves
        if (admin.getUsername().equals(usernameToDelete)) {
            return false; // Can't delete your own account
        }

        userMap.remove(usernameToDelete);
        UserDAO.saveUser(userMap);

        return true;
    }
}