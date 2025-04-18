package bto.interfaces;

/**
 * Interface for objects that can be authenticated with a user ID and password.
 */
public interface Authenticatable {
    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    String getUserID();
    /**
     * Gets the password.
     *
     * @return the password
     */
    String getPassword();
    /**
     * Changes the password to the specified value.
     *
     * @param password the new password
     */
    void changePassword(String password);
}
