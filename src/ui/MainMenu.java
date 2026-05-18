package ui;

import model.User;
import service.AuthService;
import utils.InputHelper;

public class MainMenu {



    public static void show(){
        while(true) {
            System.out.println("========================================\n" +
                    "     UNIVERSITY ERP SYSTEM\n" +
                    "========================================\n" +
                    "  1. Login\n" +
                    "  2. Sign Up(only for Admins)\n" +
                    "  3. Exit\n");

            int choice  = InputHelper.getChoice();
            switch (choice){
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleSignUp();
                    break;
                case 3:
                    System.exit(0);
                    return;
                default:
                    System.out.println("Invalid Choice!.\nEnter a valid Input.");

            }
        }
    }
    private static void handleLogin(){
        System.out.println("--- LOGIN ---");
        System.out.print("Enter user name :");
        String username = InputHelper.readLine();
        System.out.print("Enter password :");
        String password = InputHelper.readLine();
        System.out.print("Enter role (Admin/Student/Faculty): ");
        String role = InputHelper.readLine();

        User user = AuthService.login(username,password,role);
        handleMenuByRole(user);

    }

    private static void handleSignUp(){
        System.out.println("--- SIGN UP ---");
        System.out.print("Enter user name :");
        String username = InputHelper.readLine();
        System.out.print("Enter password :");
        String password = InputHelper.readLine();
        System.out.print("Enter role (Admin only): ");
        String role = InputHelper.readLine();

        if (!role.equalsIgnoreCase("Admin")) {
            System.out.println("✗ Public signup is only for Admin accounts.");
            System.out.println("  Students and Faculty are registered by the admin.");
            return;
        }

        String error = AuthService.validateSignupInputs(username, password, role);
        if (error != null) {
            System.out.println("✗ Signup failed: " + error);
            return;
        }
        boolean access = AuthService.signUp(username, password, role);
        if (access) {
            System.out.println("✓ Account created! Please login.");
        } else {
            System.out.println("✗ Signup failed. Username already taken.");
        }

    }
    public static void handleMenuByRole(User user){
        if(user==null){
            System.out.println("✗ Login failed.You don't have an account");
            return;
        }
        else{
            if(user.getRole().equals("Admin")){
                AdminMenu.show(user);
            }
            else if(user.getRole().equals("Student")){
                StudentMenu.show(user);
            }
            else if(user.getRole().equals("Faculty")){
                FacultyMenu.show(user);
            }
            else{
                System.out.println("Invalid information!");
            }
        }
    }
}
