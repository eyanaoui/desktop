package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.util.regex.Pattern;

public class InputValidation {

    public static boolean isTextFieldEmpty(String textField) {
        return textField.trim().isEmpty();
    }

    public static boolean isValidPassword(String password) {
        // Password must be at least 8 characters long and contain at least one digit and one letter
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }

    public static boolean isValidEmail(String email) {
        // Email format validation using regular expression
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }

    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
