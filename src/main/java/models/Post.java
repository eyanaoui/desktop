package models;

import java.util.Date;
import java.sql.Timestamp;

public class Post {
    private int id;
    private String titre;
    private String description;
    private String image_name;
    private String categorie;
    private Timestamp updated_at;

    public Post(int id, String titre, String description, String image_name, String categorie, Timestamp updated_at, int like_count, int dislike_count) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.image_name = image_name;
        this.categorie = categorie;
        this.updated_at = updated_at;
        this.like_count = like_count;
        this.dislike_count = dislike_count;
    }

    private int like_count;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", image_name='" + image_name + '\'' +
                ", categorie='" + categorie + '\'' +
                ", updated_at=" + updated_at +
                ", like_count=" + like_count +
                ", dislike_count=" + dislike_count +
                '}';
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getDislike_count() {
        return dislike_count;
    }

    public void setDislike_count(int dislike_count) {
        this.dislike_count = dislike_count;
    }

    private int dislike_count;



    public Post(int id, String titre, String description, String image_name, String categorie, int likeCount, int dislikeCount, Timestamp updated_at) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.image_name = image_name;
        this.categorie = categorie;
        this.updated_at = updated_at;
        this.like_count =likeCount;
        this.dislike_count=dislikeCount;
    }
    public Post(String titre, String description, String image_name, String categorie) {
        this.titre = titre;
        this.description = description;
        this.image_name = image_name;
        this.categorie = categorie;
    }

    public Post(int id, String titre, String description, String image_name, String categorie, Timestamp updated_at) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.image_name = image_name;
        this.categorie = categorie;
        this.updated_at = updated_at;
    }
    public Post(int id, String selectedTitre, String selectedCategories, String selectedDescription) {
    }

    public Post() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }


    public void setImagePath(String url) {
    }
}
