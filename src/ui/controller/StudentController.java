package ui.controller;

import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
import model.Department;
import model.Student;
import model.User;
import service.AuthService;
import service.DepartmentService;
import service.StudentService;
import ui.view.StudentView;

import java.util.List;

public class StudentController {
    private StudentView view;
    private User adminUser;

    public StudentController(User adminUser) {
        this.adminUser = adminUser;
        this.view = new StudentView(this);
        loadData();
    }

    private void loadData() {
        List<Department> depts = DepartmentService.getAllDepartments();
        view.setDepartments(FXCollections.observableArrayList(depts));

        List<Student> students = StudentService.getAllStudents();
        view.setStudents(FXCollections.observableArrayList(students));
    }

    public void saveStudent() {
        String id = view.getId();
        String name = view.getName();
        String email = view.getEmail();
        String phone = view.getPhone();
        String semStr = view.getSemester();
        String username = view.getUsername();
        String password = view.getPassword();
        Department dept = view.getDepartment();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || semStr.isEmpty() || username.isEmpty() || password.isEmpty() || dept == null) {
            view.showMessage("All fields are required!", true);
            return;
        }

        int semester;
        try {
            semester = Integer.parseInt(semStr);
        } catch (NumberFormatException e) {
            view.showMessage("Semester must be a number", true);
            return;
        }

        // 1. Create User
        if (!AuthService.isUsernameTaken(username)) {
            boolean userCreated = AuthService.adminCreateUser(adminUser, username, password, "Student");
            if (!userCreated) {
                 view.showMessage("Failed to create user credentials.", true);
                 return;
            }
        }

        // 2. Register Student
        boolean studentCreated = StudentService.registerStudent(name, email, phone, id, semester, username);
        if (!studentCreated) {
            view.showMessage("Failed to register student. ID may already exist.", true);
            return;
        }

        // 3. Assign Department
        StudentService.assignDepartment(id, dept.getDeptId());

        view.showMessage("Student saved successfully!", false);
        view.clearForm();
        loadData(); // Refresh table
    }

    public HBox getView() {
        return view.getView();
    }
}
