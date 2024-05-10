package com.example.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.User;
import services.ServiceUser;
import utils.Hash;
import utils.InputValidation;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddUser implements Initializable {

    @FXML
    private Button confirmerButton;
    @FXML
    private TextField RoleTextField;
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
    private RadioButton userRadio;

    @FXML
    private RadioButton adminRadio;

    private ListUserController listUserController;

    public void setListUserController(ListUserController listUserController) {
        this.listUserController = listUserController;
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {

    }

    @FXML
    void confirmerButtonOnAction(ActionEvent event) {

    }
    @FXML
    void insertOne(ActionEvent event) throws SQLException{
        String selectedNomUser = (String) nomUserTextField.getText();
        String selectedPrenomUser = (String) prenomUserTextField.getText();
        String selectedEmailUser = (String) emailUserTextField.getText();
        String passwordUser = passwordTextField.getText();

        String selectedRoles;
        if (adminRadio.isSelected()){ selectedRoles ="[\"ROLE_ADMIN\"]";}
        else { selectedRoles ="[]";}


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
        } else {
                    st.insertOne(user);
                    System.out.println("User added successfully.");


            if (listUserController != null) {
                        listUserController.refreshList();
                    }
                    Stage stage = (Stage) cancelButton.getScene().getWindow();
                    stage.close();
                }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to add user: " + e.getMessage());
        }
    }



    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image brandingImage = new Image(getClass().getResource("/images/logo-removebg-preview").toString());
            brandingImageView.setImage(brandingImage);
        } catch (Exception e) {
            e.printStackTrace();
        }}


}
