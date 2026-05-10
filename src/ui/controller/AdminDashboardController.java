package ui.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import ui.view.AdminDashboardView;

import java.io.File;

public class AdminDashboardController {
    private Stage primaryStage;
    private User currentUser;
    private AdminDashboardView view;

    public AdminDashboardController(Stage primaryStage, User currentUser) {
        this.primaryStage = primaryStage;
        this.currentUser = currentUser;
        this.view = new AdminDashboardView(this, currentUser);
    }

    public void showDashboard() {
        Scene scene = new Scene(view.getView(), 1280, 800);
        
        File cssFile = new File("src/ui/util/style.css");
        if (cssFile.exists()) {
            scene.getStylesheets().add("file:///" + cssFile.getAbsolutePath().replace("\\", "/"));
        }
        
        primaryStage.setTitle("University ERP System - Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true); // Dashboards look great maximized!
        primaryStage.show();
    }

    public void showStudentView() {
        StudentController studentCtrl = new StudentController(currentUser);
        view.setContent(studentCtrl.getView());
    }

    public void handleLogout() {
        // Return back to login screen safely
        LoginController loginController = new LoginController(primaryStage);
        loginController.showLoginScreen();
    }
}
