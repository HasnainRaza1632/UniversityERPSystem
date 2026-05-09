package dao;

import model.Course;
import model.Department;
import model.Faculty;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDAO {
    private static final String FILE_NAME = "src/filehandling/departments.txt";

    public static Map<String, Department> getAllDepartments() {
        Map<String, Department> deptMap = new HashMap<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return deptMap;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 3) continue;

                String deptId = data[0];
                String deptName = data[1];
                String headOfDepartment = data[2];

                Department department = new Department(deptId, deptName);
                if (!headOfDepartment.equals("null") && !headOfDepartment.isEmpty()) {
                    department.setHeadOfDepartment(headOfDepartment);
                }
                deptMap.put(deptId, department);
            }
        } catch (IOException e) {
            System.err.printf("Error reading departments: %s%n", e.getMessage());
        }
        return deptMap;
    }

    public static void saveDepartment(Map<String, Department> deptMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Department dept : deptMap.values()) {
                String hod = (dept.getHeadOfDepartment() != null && !dept.getHeadOfDepartment().isEmpty()) ? dept.getHeadOfDepartment() : "null";
                
                writer.write(dept.getDeptId() + "," + dept.getDeptName() + "," + hod);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.printf("Error saving departments: %s%n", e.getMessage());
        }
    }

    public static Department findDepartmentById(String deptId) {
        Map<String, Department> deptMap = getAllDepartments();
        return deptMap.getOrDefault(deptId, null);
    }
}
