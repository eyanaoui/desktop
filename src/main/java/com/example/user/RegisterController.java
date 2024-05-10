package com.example.user;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import models.User;
import org.controlsfx.control.Notifications;
import services.ServiceUser;
import utils.Hash;
import utils.InputValidation;
import utils.SessionManager;
import utils.MailingService;
import utils.CaptchaGenerator;



import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {


    @FXML
    private WebView captchaWebView;

    @FXML
    private Button confirmerButton;

    @FXML
    private TextField emailUserTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField nomUserTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField prenomUserTextField;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private Label captchaLabel;

    @FXML
    private TextField captchaTF;


    private final String siteKey = "6LfghYUpAAAAABhCXtTnSmlvNwjahoFhnCYzxYbR";

    @FXML
    void insertOne(ActionEvent event) throws SQLException {
        String selectedNomUser = (String) nomUserTextField.getText();
        String selectedPrenomUser = (String) prenomUserTextField.getText();
        String selectedEmailUser = (String) emailUserTextField.getText();
        String passwordUser = passwordTextField.getText();
        String selectedRoles = "[]";
        String captchaTFText = captchaTF.getText();



        String hashedpwd = Hash.generateHash(passwordUser);
// Create a new Transport object with retrieved values
        User user = new User( selectedNomUser, selectedPrenomUser, hashedpwd, selectedEmailUser, selectedRoles);

        ServiceUser st = new ServiceUser();

        try {if (InputValidation.isTextFieldEmpty(selectedNomUser)) {
            InputValidation.showAlert("Input Error", null, "Please enter a valid Name");
        } else if (InputValidation.isTextFieldEmpty(selectedPrenomUser)) {
            InputValidation.showAlert("Input Error", null, "Please enter a valid Prenom");
        } else if (!InputValidation.isValidEmail(selectedEmailUser)) {
            InputValidation.showAlert("Input Error", null, "Please enter a valid email address.");
        } else if (!InputValidation.isValidPassword(passwordUser)) {
            InputValidation.showAlert("Input Error", null, "Please enter a valid password (at least 8 characters with at least one digit and one letter).");
        } else if (!captchaLabel.getText().equals(captchaTFText)) {
            InputValidation.showAlert("Input Error", null, "Captcha is not valid");
            String captcha = CaptchaGenerator.generateCaptcha();
            System.out.println("Captcha: "+ captcha);
            captchaLabel.setText(captcha);
            captchaTF.setText("");
        } else {
            st.insertOne(user);
            System.out.println("User added successfully.");

            // Send email notification
            sendRegistrationEmail(selectedEmailUser);
            showNotification("Success", "Email reçus !!");

            redirectToFrontPage(user);
        }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to add user: " + e.getMessage());
        }
    }

    @FXML

    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image brandingImage = new Image(getClass().getResource("/images/logo-django.png").toString());
            brandingImageView.setImage(brandingImage);

           /* WebEngine webEngine = captchaWebView.getEngine();
            // Load the HTML content
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>reCAPTCHA Demo</title>\n" +
                    "    <!-- Include the reCAPTCHA API script -->\n" +
                    "    <script src=\"https://www.google.com/recaptcha/api.js\" async defer></script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h1>reCAPTCHA</h1>\n" +
                    "    <!-- Include the reCAPTCHA widget -->\n" +
                    "    <div class=\"g-recaptcha\" data-sitekey=\"6LfghYUpAAAAALwpNXErT9gjKGDMDta5U83xW_uD\n\"></div>\n" +
                    "    <!-- Your other HTML content goes here -->\n" +
                    "</body>\n" +
                    "</html>";

            webEngine.loadContent(htmlContent);*/
            //webEngine.load("https://www.google.com/recaptcha/api2/anchor?ar=1&k=" + siteKey + "&hl=en&v=r20220117140947&size=normal&cb=6m39kthokk28");
            String captcha = CaptchaGenerator.generateCaptcha();
            System.out.println("Captcha: "+ captcha);
            captchaLabel.setText(captcha);
        } catch (Exception e) {
            e.printStackTrace();
        }}

    private void navigateToLogin() {
        try {
            // Load the UpdateUser.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            javafx.scene.Parent root = loader.load();

            // Access the controller and pass the selected user to it
            LoginController controller = loader.getController();


            // Show the scene containing the UpdateUser.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    void loginLinkOnAction(ActionEvent event){

        Stage stage = (Stage) loginLink.getScene().getWindow();
        stage.close();
        // Navigate to the login window
        navigateToLogin();    }


    private void redirectToFrontPage(User user) {
        try {

            SessionManager.startSession(user);
            Parent root = FXMLLoader.load(getClass().getResource("Front.fxml"));

            Stage stage = new Stage();
            stage.setTitle("Admin - User List");
            stage.setScene(new Scene(root));
            stage.show();
            // Close the login stage
            ((Stage) emailUserTextField.getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Method to send registration email
    private void sendRegistrationEmail(String recipientEmail) {
        String subject = "Welcome to Our Application!";
        String body = "Thank you for registering with our Django desktop application. Your account has been successfully created.";

        // Send email
        MailingService.SendMail(recipientEmail, subject, body);
    }
    private void showNotification(String title, String content) {
        Notifications notification =Notifications.create()
                .title(title)
                .text(content);

        Platform.runLater(() -> notification.showInformation());
    }




}
