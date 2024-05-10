package com.example.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PopUp implements Initializable {

    @FXML
    private Button modifierButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button supprimerButton;
    @FXML
    private ImageView brandingImageView;

    private User selectedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image brandingImage = new Image(getClass().getResource("/images/logo-django.png").toString());
            brandingImageView.setImage(brandingImage);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void initData(User user) {
        this.selectedUser = user;

    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void modifierButtonClicked(ActionEvent event) {
        // Get the selected user from wherever it's stored


        // Navigate to UpdateUser.fxml
        navigateToUpdateUser(this.selectedUser);

        cancelButtonOnAction(event);
    }

    private void navigateToUpdateUser(User user) {
        try {
            // Load the UpdateUser.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateUser.fxml"));
            Parent root = loader.load();

            // Access the controller and pass the selected user to it
            UpdateUser controller = loader.getController();
            controller.initData(user);

            // Show the scene containing the UpdateUser.fxml file
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
