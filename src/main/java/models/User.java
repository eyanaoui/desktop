package models;

public class User {

    private int id;
    private String nom_user, prenom_user,password,email;
    private String roles ;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_user() {
        return nom_user;
    }

    public void setNom_user(String nom_user) {
        this.nom_user = nom_user;
    }

    public String getPrenom_user() {
        return prenom_user;
    }

    public void setPrenom_user(String prenom_user) {
        this.prenom_user = prenom_user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }



    public void setRoles(String roles) {
        this.roles = roles;
    }

    public User() {
    }

    public User(String nom_user, String prenom_user, String password, String email, String roles) {
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public User(int id, String nom_user, String prenom_user, String password, String email, String roles) {
        this.id = id;
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom_user='" + nom_user + '\'' +
                ", prenom_user='" + prenom_user + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles + '\'' +
                '}';
    }
}
