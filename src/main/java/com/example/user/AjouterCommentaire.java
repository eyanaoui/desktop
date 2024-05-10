package com.example.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Commentaire;
import models.Post;
import service.ServiceCommentaire;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AjouterCommentaire {

    @FXML
    private Label all;

    @FXML
    private Button btAjouterCommentaire;

    @FXML
    private Label cate;

    @FXML
    private Label des;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private Button retourButton;

    @FXML
    private TextField tfauteur;

    @FXML
    private TextField tfcontenu;


    @FXML
    private Label titre;

    @FXML
    private ComboBox<String> postComboBox;
    private String post_id;
    private ServiceCommentaire serviceCommentaire;
    @FXML
    private Button cancelButton;

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }




    @FXML
    void initialize() {
        serviceCommentaire = new ServiceCommentaire(); // Remove redeclaration
        ObservableList<String> postTitles;
        try {
            List<String> allPostTitles = serviceCommentaire.getAllPostTitles();
            postTitles = FXCollections.observableArrayList(allPostTitles);
        } catch (SQLException e) {
            System.err.println("Error getting post titles: " + e.getMessage());
            postTitles = FXCollections.observableArrayList();
        }
        postComboBox.setItems(postTitles);
    }


    @FXML
    void AjouterCommentaire(ActionEvent event) {
        String contenu = tfcontenu.getText().trim();
        String auteur = tfauteur.getText().trim();

        // Vérifier si les champs de texte sont vides
        if (contenu.isEmpty() || auteur.isEmpty()) {
            all.setText("You should fill all fields");
            return; // Quitter la méthode si un champ est vide
        } else {
            all.setText(""); // Réinitialiser le message d'erreur global
        }

        String selectedPostTitle = postComboBox.getValue();
        if (selectedPostTitle == null || selectedPostTitle.isEmpty()) {
            all.setText("Please select a post");
            return;
        } else {
            all.setText(""); // Réinitialiser le message d'erreur global
        }

        try {
            int postId = serviceCommentaire.getPostIdByTitle(selectedPostTitle);

            Commentaire commentaire = new Commentaire();
            commentaire.setContenu(contenu);
            commentaire.setAuteur(auteur);
            commentaire.setDate_creation(new Timestamp(System.currentTimeMillis()));
            Post post = new Post();
            post.setId(postId);
            commentaire.setPost(post);

            serviceCommentaire.insertOne(commentaire);

            tfcontenu.clear();
            tfauteur.clear();
        } catch (SQLException e) {
            System.err.println("Error inserting commentaire: " + e.getMessage());
        }
    }

    @FXML
    void retourToAfficher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AfficherCommentaire.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage from the event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }


