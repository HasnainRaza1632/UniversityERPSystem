package dao;

import model.Course;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDAO {
    private static final String FILE_NAME = "src/filehandling/courses.txt";

    public static Map<String, Course> getAllCourses() {
        Map<String, Course> courseMap = new HashMap<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return courseMap;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) continue;

                String courseId = data[0];
                String courseName = data[1];
                int creditHours = Integer.parseInt(data[2]);
                String description = data[3];

                Course course = new Course(courseId, courseName, creditHours, description, null, null);
                courseMap.put(courseId, course);
            }
        } catch (IOException e) {
            System.err.printf("Error reading courses: %s%n", e.getMessage());
        }
        return courseMap;
    }

    public static void saveCourse(Map<String, Course> courseMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Course course : courseMap.values()) {
                String facultyId = (course.getAssignedFaculty() != null) ? course.getAssignedFaculty().getFacultyId() : "null";
                String deptId = (course.getDepartment() != null) ? course.getDepartment().getDeptId() : "null";
                
                writer.write(course.getCourseId() + "," + course.getCourseName() + "," +
                        course.getCreditHours() + "," + course.getDescription() + "," +
                        facultyId + "," + deptId);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.printf("Error saving courses: %s%n", e.getMessage());
        }
    }

    public static List<Course> getCoursesByStudentId(String studentId) {
        List<Course> enrolledCourses = new ArrayList<>();
        Map<String, Course> allCourses = getAllCourses();
        
        File file = new File("src/filehandling/grades.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 2 && data[0].equals(studentId)) {
                        String courseId = data[1];
                        if (allCourses.containsKey(courseId) && !enrolledCourses.contains(allCourses.get(courseId))) {
                            enrolledCourses.add(allCourses.get(courseId));
                        }
                    }
                }
            } catch (IOException e) {
                System.err.printf("Error reading student courses from grades: %s%n", e.getMessage());
            }
        }
        return enrolledCourses;
    }

    // New method to load courses strictly by department
    public static List<Course> getCoursesByDepartmentId(String targetDeptId) {
        List<Course> deptCourses = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return deptCourses;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) continue;

                String deptId = data[5];
                if (deptId.equals(targetDeptId)) {
                    String courseId = data[0];
                    String courseName = data[1];
                    int creditHours = Integer.parseInt(data[2]);
                    String description = data[3];

                    Course course = new Course(courseId, courseName, creditHours, description, null, null);
                    deptCourses.add(course);
                }
            }
        } catch (IOException e) {
            System.err.printf("Error reading courses for department: %s%n", e.getMessage());
        }
        return deptCourses;
    }
}
