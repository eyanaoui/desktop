package service;

import utils.data;
import models.Post;
import utils.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePost implements CRUD<Post> {

    private Connection cnx;

    public ServicePost() {
        cnx = DBconnection.getInstance().getCnx();
    }

    public void insertOne(Post post) throws SQLException {
        String req = "INSERT INTO `post`(`titre`, `description`, `image_name`, `categorie`, `like_count`, `dislike_count`, `updated_at`) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setString(1, post.getTitre());
            preparedStatement.setString(2, post.getDescription());
            String path = data.path;
            path = path.replace("\\", "\\\\");
            preparedStatement.setString(3, path);
            preparedStatement.setString(4, post.getCategorie());
            preparedStatement.setInt(5, post.getLike_count());
            preparedStatement.setInt(6, post.getDislike_count());
            preparedStatement.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post Added!");
            } else {
                System.out.println("No post added.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting post: " + e.getMessage());
        }
    }

    public void updateOne(Post post) throws SQLException {


        // SQL query to update the post
        String req = "UPDATE `post` SET `titre`=?, `description`=?, `image_name`=?, `categorie`=?, `like_count`=?, `dislike_count`=?, `updated_at`=? WHERE `id`=?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            // Set parameters for the prepared statement
            preparedStatement.setString(1, post.getTitre());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setString(3, post.getImage_name());
            preparedStatement.setString(4, post.getCategorie());
            preparedStatement.setInt(5, post.getLike_count());
            preparedStatement.setInt(6, post.getDislike_count());

            // Set updated_at field
            Timestamp timestamp = (Timestamp) post.getUpdated_at();
            if (timestamp != null) {
                preparedStatement.setTimestamp(7, new java.sql.Timestamp(timestamp.getTime()));
            } else {
                preparedStatement.setNull(7, Types.TIMESTAMP);
            }

            // Set the ID parameter
            preparedStatement.setInt(8, post.getId());

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post updated successfully!");
            } else {
                System.out.println("No post updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating post: " + e.getMessage());
        }
    }

    public void deleteOne(Post post) throws SQLException {
        String req = "DELETE FROM `post` WHERE `id`=?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setInt(1, post.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post deleted successfully!");
            } else {
                System.out.println("No post deleted. Post with ID " + post.getId() + " not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting post: " + e.getMessage());
            throw e; // Rethrow the exception to propagate it to the caller
        }
    }

    @Override
    public List<Post> selectAll() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String req = "SELECT * FROM post";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String description = resultSet.getString("description");
                String image_name = resultSet.getString("image_name");
                String categorie = resultSet.getString("categorie");
                int likeCount = resultSet.getInt("like_count");
                int dislikeCount = resultSet.getInt("dislike_count");
                Timestamp updated_at = resultSet.getTimestamp("updated_at");
                System.out.println("===========================================");
                System.out.println(resultSet.getInt("id"));
                Post post = new Post(resultSet.getInt("id"), titre, description, image_name, categorie, likeCount, dislikeCount, updated_at);
                System.out.println(post);
                System.out.println("===========================================");
                posts.add(post);
            }
        } catch (SQLException e) {
            System.err.println("Error selecting posts: " + e.getMessage());
            throw e;
        }

        return posts;
    }
    public void updateLikeCount(long postId, Post post) throws SQLException {
        if (post.getTitre().isEmpty()) {
            System.err.println("Error updating post: Invalid post title.");
            return;
        }

        String req = "UPDATE `post` SET `like_count`=? WHERE `titre`=?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setInt(1, post.getLike_count());
            preparedStatement.setString(2, post.getTitre());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post like count updated successfully!");
            } else {
                System.out.println("No post like count updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating post like count: " + e.getMessage());
        }
    }

    // Rename the method for consistency
   /* public Post selectById(int postId) throws SQLException {
        String req = "SELECT * FROM post WHERE id = ?";
        Post post = null;

        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setInt(1, postId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String titre = resultSet.getString("titre");
                    String description = resultSet.getString("description");
                    String image_name = resultSet.getString("image_name");
                    String categorie = resultSet.getString("categorie");
                    int likeCount = resultSet.getInt("like_count");
                    int dislikeCount = resultSet.getInt("dislike_count");
                    Timestamp updated_at = resultSet.getTimestamp("updated_at");
                    post = new Post(postId, titre, description, image_name, categorie, likeCount, dislikeCount, updated_at);
                } else {
                    throw new IllegalArgumentException("No post found with ID: " + postId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error selecting post by ID: " + e.getMessage());
            throw e;
        }

        return post;
    }*/
}