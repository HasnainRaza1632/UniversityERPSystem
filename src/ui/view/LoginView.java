package ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import ui.controller.LoginController;

public class LoginView {
    private LoginController controller;
    private BorderPane root;
    
    private TextField txtUsername;
    private PasswordField txtPassword;
    private ComboBox<String> cmbRole;
    private Label lblMessage;

    public LoginView(LoginController controller) {
        this.controller = controller;
        createUI();
    }

    private void createUI() {
        root = new BorderPane();
        root.getStyleClass().add("root-pane");

        // Center Card
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setMaxWidth(400);
        card.setMaxHeight(450);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);

        // Logo / Title
        Label lblTitle = new Label("University ERP");
        lblTitle.getStyleClass().add("header-label");
        Label lblSubtitle = new Label("Smart Campus Management");
        lblSubtitle.getStyleClass().add("sub-header-label");
        
        VBox headerBox = new VBox(5, lblTitle, lblSubtitle);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        // Form Fields
        VBox formBox = new VBox(15);
        
        VBox userBox = new VBox(5);
        Label lblUser = new Label("Username");
        lblUser.getStyleClass().add("label-bold");
        txtUsername = new TextField();
        txtUsername.setPromptText("Enter username");
        txtUsername.getStyleClass().add("text-field");
        userBox.getChildren().addAll(lblUser, txtUsername);

        VBox passBox = new VBox(5);
        Label lblPass = new Label("Password");
        lblPass.getStyleClass().add("label-bold");
        txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter password");
        txtPassword.getStyleClass().add("password-field");
        passBox.getChildren().addAll(lblPass, txtPassword);

        VBox roleBox = new VBox(5);
        Label lblRole = new Label("Login As");
        lblRole.getStyleClass().add("label-bold");
        cmbRole = new ComboBox<>();
        cmbRole.getItems().addAll("Admin", "Student", "Faculty");
        cmbRole.setValue("Admin");
        cmbRole.setMaxWidth(Double.MAX_VALUE);
        cmbRole.getStyleClass().add("combo-box");
        roleBox.getChildren().addAll(lblRole, cmbRole);

        formBox.getChildren().addAll(userBox, passBox, roleBox);

        // Login Button
        Button btnLogin = new Button("Login");
        btnLogin.getStyleClass().add("btn-primary");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setOnAction(e -> handleLogin());

        // Message Label
        lblMessage = new Label();
        lblMessage.setWrapText(true);
        lblMessage.setAlignment(Pos.CENTER);

        card.getChildren().addAll(headerBox, formBox, btnLogin, lblMessage);
        
        root.setCenter(card);
    }

    private void handleLogin() {
        lblMessage.setText("");
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String role = cmbRole.getValue();
        
        controller.handleLogin(username, password, role);
    }

    public void showError(String message) {
        lblMessage.setText(message);
        lblMessage.getStyleClass().removeAll("text-success");
        if (!lblMessage.getStyleClass().contains("text-error")) {
            lblMessage.getStyleClass().add("text-error");
        }
    }

    public void showSuccess(String message) {
        lblMessage.setText(message);
        lblMessage.getStyleClass().removeAll("text-error");
        if (!lblMessage.getStyleClass().contains("text-success")) {
            lblMessage.getStyleClass().add("text-success");
        }
    }

    public BorderPane getView() {
        return root;
    }
}
