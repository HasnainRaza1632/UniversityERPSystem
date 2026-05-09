package service;

import model.User;

import java.util.Map;
import java.util.Scanner;
import dao.UserDAO;

public class AuthService {

    private static final Map<String, User> userMap = UserDAO.getAllUsers();
    private static final Scanner sc = new Scanner(System.in);

    public static void login(){

        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();
        System.out.print("Enter your role: ");
        String role = sc.nextLine();

        if(userMap.containsKey(username)){
            if (userMap.get(username).getPassword().equals(password)){
                if(userMap.get(username).getRole().equals(role)){
                    System.out.println("Login Successfully.");
                }else {
                    System.out.println("Incorrect role.");
                }
            }else {
                System.out.println("Please enter correct password.");
            }
        }else {
            System.out.println("User not found.");
        }
    }

    public static void signUp(){

        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        System.out.print("Enter role (Admin/Student/Faculty): ");
        String role = sc.nextLine();
        if(userMap.containsKey(username)){
            System.out.println("User already exist.");
            return;
        }
        userMap.put(username,new User(username,password,role));
        UserDAO.saveUser(userMap);
        System.out.println("Account created successfully.");
    }
}
