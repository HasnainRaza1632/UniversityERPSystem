package utils;

import java.util.Scanner;

public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);

    public static String readLine() {
        return scanner.nextLine().trim();
    }

    public static int readInt() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  Invalid number. Please enter a whole number.");
                System.out.print("  Try again: ");
            }
        }
    }

    public static double readDouble() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("  Invalid number. Please enter a valid number.");
                System.out.print("  Try again: ");
            }
        }
    }
    public static int getChoice() {
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume the leftover newline from buffer
        return choice;
    }

    public static void close() {
        scanner.close();
    }

}