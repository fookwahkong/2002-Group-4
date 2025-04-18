package bto.boundary;

/***
 * Interface defining the base UI functionality for all user bto.interfaces
 */

public interface UserInterface {
    /**
     * Displays the menu
     */
    void showMenu();
    /**
     * Processes the input
     * @param choice The choice
     */
    void processInput(int choice);
    /**
     * Displays the menu options
     */
    void displayMenuOptions();
}
