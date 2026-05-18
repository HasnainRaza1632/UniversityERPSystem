package ui;

import model.User;
import service.AuthService;
import utils.InputHelper;
import validation.UserValidator;

public class MainMenu {

    // ─── Box drawing constants (72 chars wide, 70 inner) ──────────────────────
    private static final String LINE      = "╠══════════════════════════════════════════════════════════════════════╣";
    private static final String TOP       = "╔══════════════════════════════════════════════════════════════════════╗";
    private static final String BOT       = "╚══════════════════════════════════════════════════════════════════════╝";
    private static final int    BOX_WIDTH = 70;

    /** Left-aligned row with right padding to fill the box. */
    private static String row(String content) {
        int padding = BOX_WIDTH - content.length();
        if (padding < 0) {
            content = content.substring(0, BOX_WIDTH);
            padding = 0;
        }
        return "║" + content + " ".repeat(padding) + "║";
    }

    /** Centred row inside the box. */
    private static String center(String text) {
        int space = BOX_WIDTH - text.length();
        int left  = space / 2;
        int right = space - left;
        return "║" + " ".repeat(left) + text + " ".repeat(right) + "║";
    }

    // ─── Startup banner (shown once) ──────────────────────────────────────────
    private static void printBanner() {
        System.out.println();
        System.out.println(TOP);
        System.out.println(center(""));
        System.out.println(center("*****   UNIVERSITY ERP SYSTEM   *****"));
        System.out.println(center(""));
        System.out.println(center("Manage  ·  Learn  ·  Grow  ·  Succeed"));
        System.out.println(center(""));
        System.out.println(BOT);
        System.out.println();
    }

    // ─── Main loop ────────────────────────────────────────────────────────────
    public static void show() {
        printBanner();
        while (true) {
            System.out.println(TOP);
            System.out.println(center("MAIN  MENU"));
            System.out.println(LINE);
            System.out.println(row(""));
            System.out.println(row("   [ 1 ]  Login"));
            System.out.println(row("   [ 2 ]  Sign Up          (Admin accounts only)"));
            System.out.println(row("   [ 3 ]  Exit"));
            System.out.println(row(""));
            System.out.println(BOT);
            System.out.println();

            int choice = InputHelper.getChoice();
            System.out.println();

            switch (choice) {
                case 1: handleLogin();  break;
                case 2: handleSignUp(); break;
                case 3:
                    printGoodbye();
                    System.exit(0);
                    return;
                default:
                    printError("Invalid choice. Please enter 1, 2 or 3.");
            }
        }
    }

    // ─── Login ────────────────────────────────────────────────────────────────
    private static void handleLogin() {
        System.out.println(TOP);
        System.out.println(center("LOGIN"));
        System.out.println(LINE);
        System.out.println(row("  Enter your credentials below."));
        System.out.println(BOT);
        System.out.println();

        System.out.print("  Username : ");
        String username = InputHelper.readLine();

        System.out.print("  Password : ");
        String password = InputHelper.readLine();

        System.out.print("  Role     (Admin / Student / Faculty) : ");
        String role = InputHelper.readLine();
        System.out.println();

        // Client-side validation first
        String validationError = UserValidator.validateLoginInputs(username, password, role);
        if (validationError != null) {
            printError(validationError);
            return;
        }

        User user = AuthService.login(username, password, role);
        if (user == null) {
            printError("Login failed. Incorrect username, password, or role.");
            return;
        }

        printSuccess("Welcome back, " + user.getUsername() + "!   Role: " + user.getRole());
        handleMenuByRole(user);
    }

    // ─── Sign Up ──────────────────────────────────────────────────────────────
    private static void handleSignUp() {
        System.out.println(TOP);
        System.out.println(center("SIGN UP"));
        System.out.println(LINE);
        System.out.println(row("  Admin account registration."));
        System.out.println(row("  Students & Faculty are registered by an Admin after login."));
        System.out.println(BOT);
        System.out.println();

        System.out.print("  Username : ");
        String username = InputHelper.readLine();

        System.out.print("  Password : ");
        String password = InputHelper.readLine();

        System.out.print("  Role     (Admin only) : ");
        String role = InputHelper.readLine();
        System.out.println();

        if (!role.equalsIgnoreCase("Admin")) {
            printError("Public sign-up is restricted to Admin accounts only.");
            System.out.println("  i  Students and Faculty must be registered by an Admin.");
            System.out.println();
            return;
        }

        String error = UserValidator.validateSignupInputs(username, password, role);
        if (error != null) {
            printError("Sign-up failed: " + error);
            return;
        }

        boolean success = AuthService.signUp(username, password, role);
        if (success) {
            printSuccess("Admin account created successfully! You may now log in.");
        } else {
            printError("Sign-up failed. Username \"" + username + "\" is already taken.");
        }
    }

    // ─── Role router ──────────────────────────────────────────────────────────
    public static void handleMenuByRole(User user) {
        if (user == null) return;
        switch (user.getRole()) {
            case "Admin":   AdminMenu.show(user);   break;
            case "Student": StudentMenu.show(user); break;
            case "Faculty": FacultyMenu.show(user); break;
            default:        printError("Unknown role: " + user.getRole());
        }
    }

    // ─── Feedback helpers ─────────────────────────────────────────────────────
    private static void printSuccess(String msg) {
        System.out.println("  ✔  " + msg);
        System.out.println();
    }

    private static void printError(String msg) {
        System.out.println("  ✘  " + msg);
        System.out.println();
    }

    private static void printGoodbye() {
        System.out.println();
        System.out.println(TOP);
        System.out.println(center(""));
        System.out.println(center("Thank you for using University ERP System!"));
        System.out.println(center(""));
        System.out.println(center("Goodbye!"));
        System.out.println(center(""));
        System.out.println(BOT);
        System.out.println();
    }
}
