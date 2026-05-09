package dao;

import model.Department;
import model.Faculty;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacultyDAO {
    private static final String FILE_NAME = "src/filehandling/faculties.txt";

    public static Map<String, Faculty> getAllFaculties() {
        Map<String, Faculty> facultyMap = new HashMap<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return facultyMap;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Format: facultyId, name, email, phone, designation, salary, departmentId, username
                if (data.length < 8) continue;

                String facultyId = data[0];
                String name = data[1];
                String email = data[2];
                String phone = data[3];
                String designation = data[4];
                double salary = Double.parseDouble(data[5]);
                String departmentId = data[6];
                String username = data[7];

                Department department = DepartmentDAO.findDepartmentById(departmentId);
                User user = UserDAO.findUserByUsername(username);

                // Constructor: name, email, phone, employeeId, designation, department, salary, user
                Faculty faculty = new Faculty(name, email, phone, facultyId, designation, department, salary, user);
                facultyMap.put(facultyId, faculty);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.printf("Error reading faculties: %s%n", e.getMessage());
        }
        return facultyMap;
    }

    public static void saveFaculty(Map<String, Faculty> facultyMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Faculty faculty : facultyMap.values()) {
                String deptId = (faculty.getDepartment() != null) ? faculty.getDepartment().getDeptId() : "null";
                String username = (faculty.getUser() != null) ? faculty.getUser().getUsername() : "null";

                writer.write(faculty.getFacultyId() + "," + faculty.getName() + "," +
                        faculty.getEmail() + "," + faculty.getPhone() + "," +
                        faculty.getDesignation() + "," + faculty.getSalary() + "," +
                        deptId + "," + username);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.printf("Error saving faculties: %s%n", e.getMessage());
        }
    }

    public static Faculty findFacultyById(String facultyId) {
        return getAllFaculties().get(facultyId);
    }

    public static List<Faculty> getFacultiesByDepartmentId(String targetDeptId) {
        List<Faculty> deptFaculties = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return deptFaculties;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 8) continue;

                String departmentId = data[6];
                if (departmentId.equals(targetDeptId)) {
                    String facultyId = data[0];
                    String name = data[1];
                    String email = data[2];
                    String phone = data[3];
                    String designation = data[4];
                    double salary = Double.parseDouble(data[5]);
                    String username = data[7];

                    User user = UserDAO.findUserByUsername(username);
                    Faculty faculty = new Faculty(name, email, phone, facultyId, designation, null, salary, user);
                    deptFaculties.add(faculty);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.printf("Error reading faculties for department: %s%n", e.getMessage());
        }
        return deptFaculties;
    }
}
