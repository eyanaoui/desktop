package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Commentaire {
    private int id;
    private String contenu, auteur;
    private Timestamp date_creation;
    private  Post post;
    private Connection cnx;



    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", auteur='" + auteur + '\'' +
                ", date_creation=" + date_creation +
                ", post=" + post +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setDate_creation(Timestamp date_creation) {
        this.date_creation = date_creation;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getId() {
        return id;
    }

    public String getContenu() {
        return contenu;
    }

    public String getAuteur() {
        return auteur;
    }

    public Timestamp getDate_creation() {
        return date_creation;
    }

    public Post getPost() {
        return post;
    }



}
