package ui.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import service.AuthService;
import ui.view.LoginView;

import java.io.File;

public class LoginController {
    private Stage primaryStage;
    private LoginView loginView;

    public LoginController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.loginView = new LoginView(this);
    }

    public void showLoginScreen() {
        Scene scene = new Scene(loginView.getView(), 1000, 700);
        
        File cssFile = new File("src/ui/util/style.css");
        if (cssFile.exists()) {
            scene.getStylesheets().add("file:///" + cssFile.getAbsolutePath().replace("\\", "/"));
        }
        
        primaryStage.setTitle("University ERP System - Login");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void handleLogin(String username, String password, String role) {
        String validationError = AuthService.validateLoginInputs(username, password, role);
        if (validationError != null) {
            loginView.showError(validationError);
            return;
        }

        User user = AuthService.login(username, password, role);
        if (user != null) {
            loginView.showSuccess("Login Successful! Redirecting...");
            
            if (role.equalsIgnoreCase("Admin")) {
                AdminDashboardController adminDashboard = new AdminDashboardController(primaryStage, user);
                adminDashboard.showDashboard();
            } else {
                // Future implementation for Teacher and Student
                System.out.println("Routing to " + user.getRole() + " Dashboard...");
            }
            
        } else {
            loginView.showError("Invalid credentials or role mismatch.");
        }
    }
}
