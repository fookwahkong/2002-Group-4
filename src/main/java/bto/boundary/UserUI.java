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
    
    // Default constructor
    public UserUI() {
        this.currentUser = null;
    }
    
    // Constructor with user
    public UserUI(User user) {
        this.currentUser = user;
    }
    
    // Delegate to UIUtils
    protected int getValidIntInput(int min, int max) {
        return UIUtils.getValidIntInput(min, max);
    }
    
    protected int getIntInput(String prompt) {
        return UIUtils.getIntInput(prompt);
    }
    
    protected String getStringInput(String prompt) {
        return UIUtils.getStringInput(prompt);
    }
    
    protected float getFloatInput(String prompt) {
        return UIUtils.getFloatInput(prompt);
    }

    protected LocalDate getDateInput(String prompt) {
        return UIUtils.getDateInput(prompt);
    }

    protected abstract int getMaxMenuOption();
    
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