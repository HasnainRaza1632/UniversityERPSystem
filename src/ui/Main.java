package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.controller.LoginController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginController loginController = new LoginController(primaryStage);
        loginController.showLoginScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
