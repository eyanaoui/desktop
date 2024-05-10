package service;

import models.Commentaire;
import models.Post;
import utils.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommentaire implements CRUD<Commentaire> {

        private Connection cnx;

        public ServiceCommentaire() {
                cnx = DBconnection.getInstance().getCnx();
        }

        public void insertOne(Commentaire commentaire) throws SQLException {
                if (commentaire.getContenu() == null) {
                        throw new IllegalArgumentException("Contenu cannot be null");
                }

                String req = "INSERT INTO commentaire(contenu, date_creation, auteur, post_id) VALUES (?,?,?,?)";
                PreparedStatement ps = null;
                try {
                        ps = cnx.prepareStatement(req);
                        ps.setString(1, commentaire.getContenu());
                        ps.setTimestamp(2, commentaire.getDate_creation());
                        ps.setString(3, commentaire.getAuteur());
                        if (commentaire.getPost() != null) {
                                ps.setInt(4, commentaire.getPost().getId());
                        } else {
                                ps.setNull(4, Types.INTEGER);
                        }

                        // Execute the prepared statement without passing the SQL query string again
                        ps.executeUpdate();
                } finally {
                        if (ps != null) {
                                ps.close();
                        }
                }
        }




        @Override
        public void updateOne(Commentaire commentaire) throws SQLException {
                String sql = "UPDATE commentaire SET contenu=?, date_creation=?, auteur=?, post_id=? WHERE id=?";

                try (PreparedStatement statement = cnx.prepareStatement(sql)) {
                        statement.setString(1, commentaire.getContenu());
                        statement.setTimestamp(2, commentaire.getDate_creation());
                        statement.setString(3, commentaire.getAuteur());
                        statement.setInt(4, commentaire.getPost().getId());
                        statement.setInt(5, commentaire.getId());

                        int rowsUpdated = statement.executeUpdate();

                        if (rowsUpdated == 0) {
                                System.out.println("Commentaire not found, update failed.");
                        } else {
                                System.out.println("Commentaire updated successfully.");
                        }
                }
        }

        @Override
        public void deleteOne(Commentaire commentaire) throws SQLException {
                String sql = "DELETE FROM commentaire WHERE id=?";

                try (PreparedStatement statement = cnx.prepareStatement(sql)) {
                        statement.setInt(1, commentaire.getId());

                        int rowsDeleted = statement.executeUpdate();
                        if (rowsDeleted == 0) {
                                System.out.println("Commentaire not found, delete failed.");
                        } else {
                                System.out.println("Commentaire deleted successfully.");
                        }
                }
        }

        @Override
        public List<Commentaire> selectAll() throws SQLException {
                List<Commentaire> commentaireList = new ArrayList<>();

                String req = "SELECT * FROM commentaire";
                Statement st = cnx.createStatement();

                ResultSet rs = st.executeQuery(req);

                while (rs.next()) {
                        Commentaire commentaire = new Commentaire();

                        commentaire.setId(rs.getInt("id"));
                        commentaire.setContenu(rs.getString("contenu"));
                        commentaire.setDate_creation(rs.getTimestamp("date_creation"));
                        commentaire.setAuteur(rs.getString("auteur"));

                        Post post = new Post();
                        post.setId(rs.getInt("post_id"));
                        commentaire.setPost(post);

                        commentaireList.add(commentaire);
                }

                return commentaireList;
        }

        public List<String> getAllPostTitles() throws SQLException {
                List<String> postTitles = new ArrayList<>();
                String sql = "SELECT titre FROM post";

                try (Statement statement = cnx.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)) {
                        while (resultSet.next()) {
                                String postTitle = resultSet.getString("titre");
                                postTitles.add(postTitle);
                        }
                }

                return postTitles;
        }

        public int getPostIdByTitle(String postTitle) throws SQLException {
                String sql = "SELECT id FROM post WHERE titre = ?";
                try (PreparedStatement statement = cnx.prepareStatement(sql)) {
                        statement.setString(1, postTitle);
                        try (ResultSet resultSet = statement.executeQuery()) {
                                if (resultSet.next()) {
                                        return resultSet.getInt("id");
                                }
                        }
                }
                throw new IllegalArgumentException("Post not found for title: " + postTitle);
        }

        public List<Commentaire> selectByPostId(long postId) throws SQLException {
                List<Commentaire> commentaires = new ArrayList<>();

                String req = "SELECT * FROM commentaire WHERE post_id = ?";
                try (PreparedStatement ps = cnx.prepareStatement(req)) {
                        ps.setLong(1, postId);
                        try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                        Commentaire commentaire = new Commentaire();
                                        commentaire.setId(rs.getInt("id"));
                                        commentaire.setContenu(rs.getString("contenu"));
                                        commentaire.setDate_creation(rs.getTimestamp("date_creation"));
                                        commentaire.setAuteur(rs.getString("auteur"));
                                        commentaires.add(commentaire);
                                }
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                        // Handle database exception
                }

                // Log the SQL query and comments retrieved
                System.out.println("SQL Query: " + req);
                System.out.println("Comments for post ID " + postId + ":");
                for (Commentaire commentaire : commentaires) {
                        System.out.println(commentaire.getContenu());
                }

                return commentaires;
        }

        public int getCountByPostId(long postId) throws SQLException {
                String sql = "SELECT COUNT(*) AS count FROM commentaire WHERE post_id = ?";
                try (PreparedStatement statement = cnx.prepareStatement(sql)) {
                        statement.setLong(1, postId);
                        try (ResultSet resultSet = statement.executeQuery()) {
                                if (resultSet.next()) {
                                        return resultSet.getInt("count");
                                }
                        }
                }
                return 0; // Default return value if no comments found
        }


}
