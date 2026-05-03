package dao;

import model.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {
        static String fileName = "src/filehandling/users.txt";

        public static Map<String , User> getAllUsers() {
            Map<String, User> users = new HashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while((line = reader.readLine()) != null){
                    String[] data = line.split(",");
                    if(data.length < 3) continue;
                    String userName = data[0];
                    String password = data[1];
                    String role = data[2];
                    users.put(userName,new User(userName,password,role));
                }
            }catch (IOException exception){
                System.out.printf("File not Found Exception : %s", exception.getMessage());
            }
            return users;
        }

        public static void saveUser(Map<String, User> userMap) {
            if (userMap.isEmpty()){
                System.out.println("Error: UserMap is Empty");
                return;
            }
            try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName))){
                for (Map.Entry<String , User> user : userMap.entrySet()){
                    fileWriter.write(user.getKey() + "," + user.getValue());
                    fileWriter.newLine();
                }
            } catch (IOException e) {
                System.out.printf("File not Found Exception : %s", e.getMessage());
            }
        }
}
