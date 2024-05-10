package com.example.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import models.Commentaire;
import models.Post;
import service.ServiceCommentaire;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentPopupController {


    @FXML
    private ListView<String> commentListView;

    @FXML
    private Button addButton;

    private Post post;



    public CommentPopupController() {
        this.post = null;
    }
    public CommentPopupController(Post post) {
        this.post = post;
    }

    public void initData(Post post) {
        this.post = post;
        loadComments();
    }

    private void loadComments() {
        try {
            if (post != null) {// Check if post is not null
                ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
                List<Commentaire> comments = serviceCommentaire.selectByPostId(post.getId());
                ObservableList<String> items = FXCollections.observableArrayList();
                for (Commentaire comment : comments) {
                    items.add(comment.getContenu());
                }
                commentListView.setItems(items);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private List<String> comments = new ArrayList<>(); // Initialize comments as an empty list

    public void setComments(List<String> comments) {
        this.comments = comments;
        ObservableList<String> items = FXCollections.observableArrayList(comments);
        commentListView.setItems(items);
    }

    @FXML
    private void addComment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Comment");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your comment:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(commentContent -> {
            try {
                ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
                Commentaire newComment = new Commentaire();
                newComment.setContenu(commentContent);
                newComment.setAuteur("Some default author");
                newComment.setDate_creation(new Timestamp(System.currentTimeMillis()));
                newComment.setPost(post); // Set the Post object for the new comment
                serviceCommentaire.insertOne(newComment);

                // Reload comments after adding a new comment
                loadComments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }



}
