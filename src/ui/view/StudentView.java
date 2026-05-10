package ui.view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import model.Department;
import model.Student;
import ui.controller.StudentController;

public class StudentView {
    private StudentController controller;
    private HBox root;
    
    // Form Fields
    private TextField txtId, txtName, txtEmail, txtPhone, txtSemester, txtUsername, txtPassword;
    private ComboBox<Department> cmbDepartment;
    private Label lblMessage;

    // Table
    private TableView<Student> table;

    public StudentView(StudentController controller) {
        this.controller = controller;
        createUI();
    }

    private void createUI() {
        root = new HBox(20);
        
        // 1. LEFT PANEL (Form)
        VBox formPanel = new VBox(15);
        formPanel.getStyleClass().add("card");
        formPanel.setPadding(new Insets(20));
        formPanel.setPrefWidth(350);

        Label lblTitle = new Label("Student Information");
        lblTitle.getStyleClass().add("header-label");
        lblTitle.setStyle("-fx-font-size: 18px;");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        txtId = createTextField("Student ID (e.g. S1001)");
        txtName = createTextField("Full Name");
        txtEmail = createTextField("Email");
        txtPhone = createTextField("Phone");
        txtSemester = createTextField("Semester (e.g. 1)");
        txtUsername = createTextField("System Username");
        txtPassword = createTextField("Default Password");
        
        cmbDepartment = new ComboBox<>();
        cmbDepartment.setPromptText("Select Department");
        cmbDepartment.setMaxWidth(Double.MAX_VALUE);
        cmbDepartment.getStyleClass().add("combo-box");

        int row = 0;
        grid.add(new Label("Student ID:"), 0, row); grid.add(txtId, 1, row++);
        grid.add(new Label("Name:"), 0, row); grid.add(txtName, 1, row++);
        grid.add(new Label("Email:"), 0, row); grid.add(txtEmail, 1, row++);
        grid.add(new Label("Phone:"), 0, row); grid.add(txtPhone, 1, row++);
        grid.add(new Label("Semester:"), 0, row); grid.add(txtSemester, 1, row++);
        grid.add(new Label("Department:"), 0, row); grid.add(cmbDepartment, 1, row++);
        grid.add(new Label("Username:"), 0, row); grid.add(txtUsername, 1, row++);
        grid.add(new Label("Password:"), 0, row); grid.add(txtPassword, 1, row++);

        HBox btnBox = new HBox(10);
        Button btnSave = new Button("Save");
        btnSave.getStyleClass().add("btn-primary");
        Button btnClear = new Button("Clear");
        btnClear.getStyleClass().add("btn-secondary");

        btnBox.getChildren().addAll(btnSave, btnClear);
        
        lblMessage = new Label();
        lblMessage.setWrapText(true);

        formPanel.getChildren().addAll(lblTitle, grid, btnBox, lblMessage);

        btnSave.setOnAction(e -> controller.saveStudent());
        btnClear.setOnAction(e -> clearForm());

        // 2. RIGHT PANEL (Table)
        VBox tablePanel = new VBox(10);
        tablePanel.getStyleClass().add("card");
        tablePanel.setPadding(new Insets(20));
        HBox.setHgrow(tablePanel, Priority.ALWAYS);

        Label lblTableTitle = new Label("Student List");
        lblTableTitle.getStyleClass().add("header-label");
        lblTableTitle.setStyle("-fx-font-size: 18px;");

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Student, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("regId"));

        TableColumn<Student, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Student, Integer> colSemester = new TableColumn<>("Semester");
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));

        table.getColumns().addAll(colId, colName, colEmail, colSemester);

        tablePanel.getChildren().addAll(lblTableTitle, table);

        root.getChildren().addAll(formPanel, tablePanel);
    }

    private TextField createTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("text-field");
        return tf;
    }

    public void clearForm() {
        txtId.clear();
        txtName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtSemester.clear();
        txtUsername.clear();
        txtPassword.clear();
        cmbDepartment.getSelectionModel().clearSelection();
        lblMessage.setText("");
    }

    public void showMessage(String msg, boolean isError) {
        lblMessage.setText(msg);
        lblMessage.setStyle(isError ? "-fx-text-fill: red; -fx-font-size: 12px;" : "-fx-text-fill: green; -fx-font-size: 12px;");
    }

    public HBox getView() { return root; }
    public String getId() { return txtId.getText().trim(); }
    public String getName() { return txtName.getText().trim(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getPhone() { return txtPhone.getText().trim(); }
    public String getSemester() { return txtSemester.getText().trim(); }
    public String getUsername() { return txtUsername.getText().trim(); }
    public String getPassword() { return txtPassword.getText().trim(); }
    public Department getDepartment() { return cmbDepartment.getValue(); }

    public void setDepartments(ObservableList<Department> depts) {
        cmbDepartment.setItems(depts);
    }

    public void setStudents(ObservableList<Student> students) {
        table.setItems(students);
    }
}
