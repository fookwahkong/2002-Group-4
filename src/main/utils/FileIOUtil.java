package main.utils;

import main.entity.user.User;
import main.enums.MaritalStatus;
import main.enums.UserRole;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIOUtil {
    // Temporarily use users.csv
    // To extend functionality once proper user classes are added

    private static final String USERS_FILE = "main/data/users.csv";

    public static List<User> loadUsers() {
        // return a list of user objects temporarily
        // can consider creating UserFactory for proper instantiation

        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;

            // consume header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String userID = parts[1];
                int age = Integer.parseInt(parts[2]);

                MaritalStatus maritalStatus = null;
                if (parts[3].equals("Single")) {
                    maritalStatus = MaritalStatus.SINGLE;
                } else if (parts[3].equals("Married")){
                    maritalStatus = MaritalStatus.MARRIED;
                }

                String password = parts[4];

                // default as applicant first, to be changed
                UserRole role = UserRole.APPLICANT;

                users.add(new User(userID, password, age, maritalStatus, role));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

}
