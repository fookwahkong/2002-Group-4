package bto.boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import bto.controller.user.UserManager;
import bto.entity.user.User;

public abstract class UserUI implements UserInterface{
    protected static final Scanner scanner = new Scanner(System.in);
    protected static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected User currentUser;
    
    /**
     * Default constructor.
     */
    public UserUI() {
        this.currentUser = null;
    }
    
    /**
     * Constructor with user.
     * 
     * @param user the user to be displayed in the UI
     */
    public UserUI(User user) {
        this.currentUser = user;
    }
    
    /**
     * Get the valid integer input.
     * 
     * @param min the minimum value
     * @param max the maximum value
     * @return the valid integer input
     */
    protected int getValidIntInput(int min, int max) {
        return UIUtils.getValidIntInput(min, max);
    }


    /**
     * Get the integer input.   
     * 
     * @param prompt the prompt to display
     * @return the integer input
     */
    protected int getIntInput(String prompt) {
        return UIUtils.getIntInput(prompt);
    }
    
    /**
     * Get the string input.
     * 
     * @param prompt the prompt to display
     * @return the string input
     */
    protected String getStringInput(String prompt) {
        return UIUtils.getStringInput(prompt);
    }
    
    /**
     * Get the float input.
     * 
     * @param prompt the prompt to display
     * @return the float input
     */
    protected float getFloatInput(String prompt) {
        return UIUtils.getFloatInput(prompt);
    }

    /**
     * Get the date input.
     * 
     * @param prompt the prompt to display
     * @return the date input
     */
    protected LocalDate getDateInput(String prompt) {
        return UIUtils.getDateInput(prompt);
    }

    /**
     * Get the maximum menu option.
     * 
     * @return the maximum menu option
     */
    protected abstract int getMaxMenuOption();
    
    /**
     * Show the menu.
     */
    public void showMenu() {
        boolean running = true;
        while (running) {
            displayMenuOptions();
            
            try {
                int choice = getValidIntInput(0, getMaxMenuOption());
                if (choice == 0) {
                    running = false;
                } else {
                    processInput(choice);
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        UserManager.getInstance().logout();
        new LoginUI().startLogin();
    }
    
}