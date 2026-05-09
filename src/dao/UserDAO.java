package dao;

import model.User;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    private static final String FILE_NAME = "src/filehandling/users.txt";

    public static Map<String, User> getAllUsers() {
        Map<String, User> users = new HashMap<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 3) continue;

                String userName = data[0].trim();
                String password = data[1].trim();
                String role = data[2].trim();
                users.put(userName, new User(userName, password, role));
            }
        } catch (IOException e) {
            System.err.printf("Error reading users: %s%n", e.getMessage());
        }
        return users;
    }

    public static void saveUser(Map<String, User> userMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : userMap.values()) {
                writer.write(user.getUsername() + "," +
                        user.getPassword() + "," +
                        user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.printf("Error saving users: %s%n", e.getMessage());
        }
    }

    public static User findUserByUsername(String Username){
        Map<String,User> userMap = getAllUsers();
        if(userMap.containsKey(Username)){
            return userMap.get(Username);
        }
        return null;
    }

    public static void deleteUser(User user){
        Map<String,User> userMap = getAllUsers();
        if(!userMap.containsKey(user.getUsername())){
            System.out.println("Error: user not found!");
            return;
        }
        userMap.remove(user.getUsername());
        saveUser(userMap);
    }
}