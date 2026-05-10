package ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.User;
import ui.controller.AdminDashboardController;

import java.awt.*;

public class AdminDashboardView {
    private AdminDashboardController controller;
    private BorderPane root;
    private User currentUser;
    private StackPane contentArea;

    public AdminDashboardView(AdminDashboardController controller, User currentUser) {
        this.controller = controller;
        this.currentUser = currentUser;
        createUI();
    }

    private void createUI() {
        root = new BorderPane();
        root.getStyleClass().add("root-pane");

        // 1. Sidebar (Left) - Dark Blue Theme
        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("sidebar-admin");
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(30, 0, 20, 0));

        // Logo/Brand area
        VBox brandBox = new VBox(5);
        brandBox.setAlignment(Pos.CENTER);
        brandBox.setPadding(new Insets(0, 0, 30, 0));
        Label lblBrand = new Label("UNIVERSITY ERP");
        lblBrand.getStyleClass().add("sidebar-title");
        Label lblBrandSub = new Label("Smart Campus Management");
        lblBrandSub.getStyleClass().add("sidebar-subtitle");
        brandBox.getChildren().addAll(lblBrand, lblBrandSub);

        // User Info area
        VBox userBox = new VBox(5);
        userBox.setAlignment(Pos.CENTER);
        userBox.setPadding(new Insets(0, 0, 40, 0));
        Label lblUser = new Label(currentUser.getUsername());
        lblUser.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        Label lblRole = new Label("System Administrator");
        lblRole.setStyle("-fx-text-fill: #10b981; -fx-font-size: 12px;"); // Green online indicator logic
        userBox.getChildren().addAll(lblUser, lblRole);

        // Menu Buttons
        VBox menuBox = new VBox(5);
        Button btnDashboard = createSidebarButton("Dashboard", true);
        Button btnStudents = createSidebarButton("Students", false);
        Button btnFaculty = createSidebarButton("Faculty", false);
        Button btnDepartments = createSidebarButton("Departments", false);
        Button btnCourses = createSidebarButton("Courses", false);
        Button btnExams = createSidebarButton("Exams & Grades", false);
        
        // Add listeners
        btnStudents.setOnAction(e -> controller.showStudentView());
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button btnLogout = createSidebarButton("Logout", false);
        btnLogout.setStyle("-fx-text-fill: #ef4444;"); // Red tint for logout

        btnLogout.setOnAction(e -> controller.handleLogout());

        menuBox.getChildren().addAll(btnDashboard, btnStudents, btnFaculty, btnDepartments, btnCourses, btnExams);
        sidebar.getChildren().addAll(brandBox, userBox, menuBox, spacer, btnLogout);

        // 2. Main Content Area
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));
        
        // Build the Dashboard Home layout matching your image
        VBox dashboardHome = buildDashboardHome();
        contentArea.getChildren().add(dashboardHome);

        root.setLeft(sidebar);
        root.setCenter(contentArea);
    }

    private Button createSidebarButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add(isActive ? "sidebar-button-active" : "sidebar-button");
        return btn;
    }

    private VBox buildDashboardHome() {
        VBox home = new VBox(30);
        
        // Top Header Row
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = new Label("Dashboard");
        lblTitle.getStyleClass().add("header-label");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label lblDate = new Label("May 2026"); // Can be dynamic
        lblDate.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        header.getChildren().addAll(lblTitle, spacer, lblDate);

        // Welcome Banner
        VBox welcomeBanner = new VBox(10);
        welcomeBanner.setPadding(new Insets(30));
        welcomeBanner.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        Label lblWelcome = new Label("Welcome back, " + currentUser.getUsername() + "!");
        lblWelcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Label lblWelcomeSub = new Label("Manage all university operations from one place.");
        lblWelcomeSub.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        welcomeBanner.getChildren().addAll(lblWelcome, lblWelcomeSub);

        // Stat Cards Row
        HBox statsRow = new HBox(20);
        // Will be made dynamic with DAO calls later
        statsRow.getChildren().addAll(
            createStatCard("Students", "120", "#3b82f6"), // Blue
            createStatCard("Faculty", "45", "#10b981"),   // Green
            createStatCard("Departments", "8", "#8b5cf6"), // Purple
            createStatCard("Courses", "32", "#f59e0b")    // Orange
        );

        // Quick Access Row
        VBox quickAccessBox = new VBox(15);
        Label lblQA = new Label("Quick Access");
        lblQA.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        HBox qaButtons = new HBox(15);
        qaButtons.getChildren().addAll(
            createQuickAccessBtn("Add Student", "btn-primary"),
            createQuickAccessBtn("Add Faculty", "btn-success"),
            createQuickAccessBtn("Add Course", "btn-secondary"),
            createQuickAccessBtn("Assign Course", "btn-secondary")
        );
        quickAccessBox.getChildren().addAll(lblQA, qaButtons);

        home.getChildren().addAll(header, welcomeBanner, statsRow, quickAccessBox);
        return home;
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(25));
        card.setPrefWidth(220);
        HBox.setHgrow(card, Priority.ALWAYS);
        
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-text-fill: #64748b; -fx-font-size: 16px;");
        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 32px; -fx-font-weight: bold;");
        
        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    private Button createQuickAccessBtn(String text, String styleClass) {
        Button btn = new Button(text);
        btn.getStyleClass().add(styleClass);
        btn.setPrefWidth(140);
        btn.setPrefHeight(45);
        return btn;
    }

    public void setContent(javafx.scene.Node node) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(node);
    }

    public BorderPane getView() {
        return root;
    }
}
