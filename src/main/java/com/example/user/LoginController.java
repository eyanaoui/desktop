package com.example.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.User;
import services.ServiceUser;
import utils.Hash;
import utils.SessionManager;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField emailUserTextField;

    @FXML
    private PasswordField entrerPasswordField;

    @FXML
    private Label loginMessagelabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Button loginButton;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private ImageView lockImageView;

    @FXML
    private Hyperlink signupLink;

    ServiceUser serviceUser = new ServiceUser();

    public static final String ACCOUNT_SID = "ACd071a31b513ba67edc3694749a1c9409";
    public static final String AUTH_TOKEN = "f467a08651531de9af832a3c7971c3a5";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image brandingImage = new Image(getClass().getResource("/images/lock-removebg-preview.png").toString());
            brandingImageView.setImage(brandingImage);

            Image lockImage = new Image(getClass().getResource("/images/lock-removebg-preview.png").toString());
            lockImageView.setImage(lockImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void LOGINUPTF(ActionEvent actionEvent) {
        String email = emailUserTextField.getText();
        String password = entrerPasswordField.getText();

        User user = serviceUser.getByEmail(email);

        if (user != null) {
            boolean isLogged = Hash.verifyHash(password, user.getPassword());
            if (isLogged) {
                // Successful login
                SessionManager.startSession(user);
                showAlert("Login Successful", "Welcome, " + user.getEmail() + "!");
                if ("[\"ROLE_ADMIN\"]".equals(user.getRoles())) {
                    // Redirect to Admin page (ListUser)
                    redirectToAdminPage();
                    String recipientPhoneNumber = "+21621796790";
                    String messageBody = "You logged in !!";
                    sendSMS(recipientPhoneNumber, messageBody);
                    showNotification("Success", "You Logged In !!");
                } else {
                    redirectToFrontPage();
                    String recipientPhoneNumber = "+21621796790";
                    String messageBody = "You logged in !!";
                    sendSMS(recipientPhoneNumber, messageBody);
                    showNotification("Success", "You Logged In !!");
                }
            } else {
                // Password verification failed
                showAlert("Login Failed", "Invalid email or password.");
            }
        } else {
            // User not found
            showAlert("Login Failed", "No user found with the provided email.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void redirectToAdminPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ListUser.fxml"));
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

    private void redirectToFrontPage() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FrontC.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Front User");
            stage.setScene(new Scene(root));
            stage.show();
            // Close the login stage
            ((Stage) emailUserTextField.getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void navigateToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
            Parent root = loader.load();
            RegisterController controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signupLinkOnAction(ActionEvent event) {
        Stage stage = (Stage) signupLink.getScene().getWindow();
        stage.close();
        navigateToSignUp();
    }

    public void ForgetPassword_btn(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ForgetPasswordForm.fxml"));
            Parent root = loader.load();

            emailUserTextField.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendSMS(String recipientPhoneNumber, String messageBody) {
        String twilioPhoneNumber = "+16165204195";

        Message message = Message.creator(
                        new PhoneNumber(recipientPhoneNumber),
                        new PhoneNumber(twilioPhoneNumber),
                        messageBody)
                .create();

        System.out.println("SMS sent successfully. SID: " + message.getSid());
    }

    private void showNotification(String title, String content) {
        Notifications.create()
                .title(title)
                .text(content)
                .showInformation();
    }


}
