package service;

import dao.UserDAO;
import model.User;
import validation.UserValidator;

import java.util.Map;

public class AuthService {

    /**
     * Public signup — Only for Student and Faculty.
     * Admin accounts can only be created by existing admins.
     */
    public static boolean signUp(String username, String password, String role) {
        String validationError = UserValidator.validateSignupInputs(username, password, role);
        if (validationError != null) {
            return false;
        }

        username = username.trim();
        password = password.trim();
        role     = role.trim();

        // Block admin signup from public registration
        if (role.equalsIgnoreCase("Admin")) {
            return false;
        }

        Map<String, User> userMap = UserDAO.getAllUsers();
        if (userMap.containsKey(username)) {
            return false;
        }

        role = UserValidator.normalizeRole(role);
        if (role == null || role.equals("Admin")) {
            return false;
        }

        userMap.put(username, new User(username, password, role));
        UserDAO.saveUser(userMap);
        return true;
    }

    /**
     * Admin-only signup — Can create any type of account.
     * Requires admin authentication.
     */
    public static boolean adminCreateUser(User admin, String username, String password, String role) {
        if (admin == null || !admin.getRole().equalsIgnoreCase("Admin")) {
            return false;
        }

        String validationError = UserValidator.validateSignupInputs(username, password, role);
        if (validationError != null) {
            return false;
        }

        Map<String, User> userMap = UserDAO.getAllUsers();

        username = username.trim();
        password = password.trim();
        role     = role.trim();

        if (userMap.containsKey(username)) {
            return false;
        }

        role = UserValidator.normalizeRole(role);
        if (role == null) {
            return false;
        }

        userMap.put(username, new User(username, password, role));
        UserDAO.saveUser(userMap);
        return true;
    }

    /**
     * Authenticate a user by username, password, and role.
     */
    public static User login(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) return null;
        if (password == null || password.trim().isEmpty()) return null;
        if (role     == null || role.trim().isEmpty())     return null;

        Map<String, User> userMap = UserDAO.getAllUsers();

        username = username.trim();
        password = password.trim();
        role     = role.trim();

        if (!userMap.containsKey(username)) return null;

        User user = userMap.get(username);
        if (!user.getPassword().equals(password))        return null;
        if (!user.getRole().equalsIgnoreCase(role))      return null;

        return user;
    }

    /**
     * Check if a username is already taken.
     */
    public static boolean isUsernameTaken(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        Map<String, User> userMap = UserDAO.getAllUsers();
        return userMap.containsKey(username.trim());
    }

    /**
     * Retrieve a user object by username.
     */
    public static User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) return null;
        Map<String, User> userMap = UserDAO.getAllUsers();
        return userMap.get(username.trim());
    }

    /**
     * Change a user's password after verifying the old password.
     */
    public static boolean changePassword(String username, String oldPassword, String newPassword) {
        if (username == null || oldPassword == null || newPassword == null) return false;

        Map<String, User> userMap = UserDAO.getAllUsers();
        if (!userMap.containsKey(username)) return false;

        User user = userMap.get(username);
        if (!user.getPassword().equals(oldPassword)) return false;
        if (newPassword.trim().length() < 6)         return false;

        user.setPassword(newPassword.trim());
        UserDAO.saveUser(userMap);
        return true;
    }

    /**
     * Delete a user account — admin-only operation.
     */
    public static boolean deleteUser(User admin, String usernameToDelete) {
        if (admin == null || !admin.getRole().equalsIgnoreCase("Admin")) return false;
        if (usernameToDelete == null || usernameToDelete.trim().isEmpty()) return false;

        Map<String, User> userMap = UserDAO.getAllUsers();
        if (!userMap.containsKey(usernameToDelete)) return false;

        // Prevent admin from deleting their own account
        if (admin.getUsername().equals(usernameToDelete)) return false;

        userMap.remove(usernameToDelete);
        UserDAO.saveUser(userMap);
        return true;
    }
}