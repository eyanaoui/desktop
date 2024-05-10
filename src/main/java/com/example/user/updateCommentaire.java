package com.example.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Commentaire;
import service.ServiceCommentaire;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class updateCommentaire implements Initializable {

    @FXML
    private Label all;

    @FXML
    private Button btmodifier;

    @FXML
    private Button cancelButton;

    @FXML
    private Label des;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private TextField tfAuteur;

    @FXML
    private TextArea tfContenu;

    @FXML
    private ImageView brandingImageView;



    private final ServiceCommentaire sp = new ServiceCommentaire();
    private Commentaire selectedCommentaire;


    public void initData(Commentaire commentaire) {
        selectedCommentaire = commentaire;
        tfAuteur.setText(selectedCommentaire.getAuteur());
        tfContenu.setText(selectedCommentaire.getContenu());
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void updateOne(ActionEvent event) {
        String selectedAuteur = tfAuteur.getText();
        String selectedContenu = tfContenu.getText();

        // Modify the existing Commentaire object with the updated values
        selectedCommentaire.setAuteur(selectedAuteur);
        selectedCommentaire.setContenu(selectedContenu);

        try {
            sp.updateOne(selectedCommentaire); // Pass the modified Commentaire object
            System.out.println("Commentaire updated successfully.");

            if (afficherCommentaire != null) {
                afficherCommentaire.refreshList();
            }

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to update commentaire: " + e.getMessage());
        }
    }

    private AfficherCommentaire afficherCommentaire ;

    public void setAfficherCommentaire(AfficherCommentaire afficherCommentaire) {
        this.afficherCommentaire = afficherCommentaire;
    }


    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Image brandingImage = new Image(getClass().getResource("/image/logo-removebg-preview.png").toString());
            brandingImageView.setImage(brandingImage);
        } catch (Exception e) {
            e.printStackTrace();
        }}
}