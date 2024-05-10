/*
package com.example.django2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.Post;

public class ItemController {
    @FXML
    private Label categorieLable;

    @FXML
    private Label desriptionLable;

    @FXML
    private ImageView img;

    @FXML
    private Label timeLable;

    @FXML
    private Label titreLabel;

    @FXML
    private void click(MouseEvent mouseEvent) {
        myListener.onClickListener(post);
    }

    private Post post;
    private MyListener myListener;

    public void setData(Post post, MyListener myListener) {
        this.post = post;
        this.myListener = myListener;
        titreLabel.setText(post.getTitre());
        desriptionLable.setText(post.getDescription());
        categorieLable.setText(post.getCategorie());



        Image image = new Image(getClass().getResource("/image/logo-removebg-preview.png").toString());
        img.setImage(image);
    }


}*/
