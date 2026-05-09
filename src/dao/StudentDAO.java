package dao;

import model.Course;
import model.Department;
import model.Grade;
import model.Student;
import model.User;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDAO {
    private static final String FILE_NAME = "src/filehandling/students.txt";

    public static Map<String, Student> getAllStudents() {
        Map<String, Student> studentMap = new HashMap<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return studentMap;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 7) continue;

                String regId = data[0];
                String name = data[1];
                String email = data[2];
                String phone = data[3];
                int semester = Integer.parseInt(data[4]);
                String departmentId = data[5];
                String username = data[6];

                Department department = DepartmentDAO.findDepartmentById(departmentId);
                User user = UserDAO.findUserByUsername(username);

                Student student = new Student(name, email, phone, regId, semester, user);
                student.setDepartment(department);

                List<Course> courses = CourseDAO.getCoursesByStudentId(regId);
                List<Grade> grades = GradeDAO.getGradesByStudentId(regId);

                for (Grade grade : grades) {
                    grade.setStudent(student);
                }

                student.setCourseList(courses);
                student.setGradesList(grades);

                studentMap.put(regId, student);
            }
        } catch (IOException e) {
            System.err.printf("Error reading students: %s%n", e.getMessage());
        }
        return studentMap;
    }

    public static Student getStudentOnlyById(String targetRegId) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 7) continue;

                String regId = data[0];
                if (regId.equals(targetRegId)) {
                    String name = data[1];
                    String email = data[2];
                    String phone = data[3];
                    int semester = Integer.parseInt(data[4]);
                    String departmentId = data[5];
                    String username = data[6];

                    Department department = DepartmentDAO.findDepartmentById(departmentId);
                    User user = UserDAO.findUserByUsername(username);

                    Student student = new Student(name, email, phone, regId, semester, user);
                    student.setDepartment(department);

                    return student;
                }
            }
        } catch (IOException e) {
            System.err.printf("Error reading students: %s%n", e.getMessage());
        }
        return null;
    }

    public static void saveStudent(Map<String, Student> studentMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student student : studentMap.values()) {
                String deptId = (student.getDepartment() != null) ? student.getDepartment().getDeptId() : "null";
                String username = (student.getUser() != null) ? student.getUser().getUsername() : "null";

                writer.write(student.getRegId() + "," + student.getName() + "," +
                        student.getEmail() + "," + student.getPhone() + "," +
                        student.getSemester() + "," + deptId + "," + username);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.printf("Error saving students: %s%n", e.getMessage());
        }
    }
}