package bto.boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * A utility class for UI operations.
 */
public class UIUtils {
    private static final Scanner scanner = new Scanner(System.in);
    protected static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
    /**
     * Gets a valid integer input within a specified range
     * @param min minimum acceptable value
     * @param max maximum acceptable value
     * @return validated integer input
     */
    public static int getValidIntInput(int min, int max) {
        int input;
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                input = Integer.parseInt(line);
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    /**
     * Gets integer input without validation
     * @param prompt message to display to user
     * @return integer input
     */
    public static int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return getIntInput(prompt);
        }
    }
    
    /**
     * Gets string input from user
     * @param prompt message to display to user
     * @return string input
     */
    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Gets float input from user
     * @param prompt message to display to user
     * @return float input
     */
    public static float getFloatInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Float.parseFloat(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Gets date input from user
     * @param prompt message to display to user
     * @return date input
     */
    public static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }

    /**
     * Displays a list of menu options
     * @param menuOptions array of strings to display
     */
    public static void displayMenuOptions(String[] menuOptions) {
        for (String option : menuOptions) {
            System.out.println(option);
        }
    }
}