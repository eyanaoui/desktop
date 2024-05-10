package utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    private static final String URL = "jdbc:mysql://localhost:3306/vintage1";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    //Second Step: Creer une instance static de meme type que la classe
    private static DBconnection instance;

    private Connection cnx;

    //First Step: Rendre le constructeur privé
    private DBconnection() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected To DATABASE !");
        } catch (SQLException e) {
            System.err.println("Error: "+e.getMessage());
        }
    }

    //Thrid Step: Creer une methode static pour recuperer l'instance

    public static DBconnection getInstance(){
        if (instance == null) instance = new DBconnection();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
