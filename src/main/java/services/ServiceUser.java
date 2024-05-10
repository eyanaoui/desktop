package services;

import models.User;
import utils.DBconnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements CRUD<User> {
    private Connection cnx ;
    public ServiceUser() {
        cnx = DBconnection.getInstance().getCnx();
    }

    /*@Override
    public void insertOne(User user) throws SQLException {
        String req = "INSERT INTO `user`(`nom_user`, `prenom_user`, `email` , `roles` , `password`) VALUES " +
                "('"+user.getNom_user()+"','"+user.getPrenom_user()+"',"+user.getEmail()+","+ user.getRoles() +" ,"+user.getPassword()+")";
        Statement st = cnx.createStatement();
        st.executeUpdate(req);
        System.out.println("User Added !");
    }*/



    @Override
    public void insertOne(User user) throws SQLException {
        String req = "INSERT INTO `user`(`nom_user`, `prenom_user`, `email` , `roles` , `password`) VALUES " +
                "(?,?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, user.getNom_user());
        ps.setString(2, user.getPrenom_user());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getRoles());
        ps.setString(5, user.getPassword());

        // Execute the prepared statement without passing the SQL query string again
        ps.executeUpdate();
    }

    @Override
    public void updateOne(User user) throws SQLException {
        String sql = "UPDATE user SET nom_user=?, prenom_user=?, password=?, email=?, roles=? WHERE id=?";

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, user.getNom_user());
            statement.setString(2, user.getPrenom_user());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getRoles());
            statement.setInt(6, user.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("User not found, update failed.");
            } else {
                System.out.println("User updated successfully.");
            }
        }
    }




    // Delete user
    @Override
    public void deleteOne(User user) throws SQLException {
        String sql = "DELETE FROM user WHERE id=?";

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setInt(1, user.getId());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                System.out.println("User not found, delete failed.");
            } else {
                System.out.println("User deleted successfully.");
            }
        }
    }

    @Override
    public List<User> selectAll() throws SQLException {
        List<User> userList = new ArrayList<>();

        String req = "SELECT * FROM `user`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            User u = new User();

            u.setId(rs.getInt("id"));
            u.setNom_user(rs.getString("nom_user"));
            u.setPrenom_user(rs.getString("prenom_user"));
            u.setEmail(rs.getString("email"));

            String roles = rs.getString("roles");

            u.setRoles(roles);

            u.setPassword(rs.getString("password"));

            userList.add(u);
        }

        return userList;
    }


    public User getByEmail(String email) {
        User user = null;
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setNom_user(rs.getString("nom_user"));
                user.setPrenom_user(rs.getString("prenom_user"));
                user.setEmail(rs.getString("email"));
                user.setRoles(rs.getString("roles"));
                user.setPassword(rs.getString("password"));

            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return user;
    }


    public void updatePassword(User t, String email) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, t.getPassword());
            ps.setString(2, email);
            ps.executeUpdate();
        }
    }
}
