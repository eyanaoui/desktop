package com.example.user;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.User;
import services.ServiceUser;
import utils.Generator;
import utils.Hash;
import utils.MailingService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ForgetPasswordFormController implements Initializable {

    @FXML
    private TextField emailCode;
    @FXML
    private TextField code;

    @FXML
    private AnchorPane get_verif;


    @FXML
    private Label labelGetCode;
    private String GeneratedCode="";


    @FXML
    private AnchorPane ConfirmNewPasswordPage;
    @FXML
    private TextField ConfirmNewPassword;
    @FXML
    private TextField NewPassword1;
    @FXML
    private Hyperlink retourLink;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



    private ServiceUser userService = new ServiceUser();

    @FXML
    public void verifbtn(ActionEvent actionEvent) {

        // Assuming you have the email of the user to update
        String userEmail = emailCode.getText();

        // Get the user from the database
        User existingUser = userService.getByEmail(userEmail);

        // Check if the user exists
        if (existingUser == null) {
            System.out.println("User not found with email: " + userEmail);
            return;
        } else {
            if (code.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Code REQUIRED ! ");
                alert.setContentText("Code REQUIRED ");
                alert.showAndWait();
            } else {
                if (GeneratedCode.equals(code.getText())) {
                    ConfirmNewPasswordPage.setVisible(true);

                    get_verif.setVisible(false);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Code NOT VALID ! ");
                    alert.setContentText("Code NOT VALID ");
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    public void GetCodeClick(ActionEvent actionEvent) {
        String email=emailCode.getText();
        if(email.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MAIL REQUIRED ! ");
            alert.setContentText("MAIL REQUIRED ");
            alert.showAndWait();

        }else{
            User u=userService.getByEmail(email);
            if(u==null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("NOt valid mail ! ");
                alert.setContentText("NOT VALID MAIL ");
                alert.showAndWait();

            }else{
                GeneratedCode= Generator.generateCode(5);
                MailingService.SendMail(email,"ResetPassword",GeneratedCode);
                labelGetCode.setText("Check your email");

            }
        }
    }

    @FXML
    public void ConfirmBTN(ActionEvent actionEvent) {
        // Retrieve updated information from text fields
        String newPassword = NewPassword1.getText();
        String confirmNewPassword = ConfirmNewPassword.getText();

        // Validate if the new password and confirm password match
        if (!newPassword.equals(confirmNewPassword)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Mismatch");
            alert.setContentText("New password and confirm password do not match.");
            alert.showAndWait();
            return;
        }

        // Assuming you have the email of the user to update
        String userEmail = emailCode.getText(); // Fix: Use emailCode.getText() to get the email

        try {
            // Get the user from the database
            User existingUser = userService.getByEmail(userEmail);

            // Check if the user exists
            if (existingUser == null) {
                System.out.println("User not found with email: " + userEmail);
                return;
            }

            // Create a User object with updated information
            User updatedUser = new User();
            String hashedNewPassword = Hash.generateHash(newPassword);
            updatedUser.setPassword(hashedNewPassword);

            // Call the ServiceUser update method
            userService.updatePassword(updatedUser, userEmail);
            System.out.println("User details updated successfully.");
            System.out.println(updatedUser);

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Login.fxml"));
            emailCode.getScene().setRoot(fxmlLoader.load());

        } catch (SQLException e) {
            System.out.println("Error updating user details: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void Retour(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            retourLink.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Retour2(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ForgetPasswordForm.fxml"));
            Parent root = loader.load();

            retourLink.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}




