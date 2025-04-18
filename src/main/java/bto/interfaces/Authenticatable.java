package bto.interfaces;

public interface Authenticatable {
    String getUserID();
    String getPassword();
    void changePassword(String password);
}
