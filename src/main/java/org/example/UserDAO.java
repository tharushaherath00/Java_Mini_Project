package org.example;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public static void addUser(User user, String adminId) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            String sql = "INSERT INTO User (NIC, Name, Password, Email, DOB, Department_ID, Department, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getNIC());
                stmt.setString(2, user.getName());
                stmt.setString(3, hashedPassword);
                stmt.setString(4, user.getEmail());
                stmt.setDate(5, user.getDOB());
                stmt.setString(6, user.getDepartmentId());
                stmt.setString(7, user.getDepartment());
                stmt.setString(8, user.getRole().getValue());
                stmt.executeUpdate();
            }

            if (user.getRole() == Role.ADMIN && !adminId.isEmpty()) {
                sql = "INSERT INTO Admin (NIC, Admin_ID) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, user.getNIC());
                    stmt.setString(2, adminId);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                        rs.getString("NIC"),
                        rs.getString("Name"),
                        rs.getString("Password"),
                        rs.getString("Email"),
                        rs.getDate("DOB"),
                        rs.getString("Department_ID"),
                        rs.getString("Department"),
                        Role.fromString(rs.getString("Role"))
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static String getAdminId(String nic) {
        String sql = "SELECT Admin_ID FROM Admin WHERE NIC = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nic);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Admin_ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateUser(User user, String adminId) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            String hashedPassword = user.getPassword().isEmpty() ? null : BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            String sql = "UPDATE User SET Name = ?, Password = COALESCE(?, Password), Email = ?, DOB = ?, Department_ID = ?, Department = ?, Role = ? WHERE NIC = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getName());
                stmt.setString(2, hashedPassword);
                stmt.setString(3, user.getEmail());
                stmt.setDate(4, user.getDOB());
                stmt.setString(5, user.getDepartmentId());
                stmt.setString(6, user.getDepartment());
                stmt.setString(7, user.getRole().getValue());
                stmt.setString(8, user.getNIC());
                stmt.executeUpdate();
            }

            sql = "DELETE FROM Admin WHERE NIC = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getNIC());
                stmt.executeUpdate();
            }

            if (user.getRole() == Role.ADMIN && !adminId.isEmpty()) {
                sql = "INSERT INTO Admin (NIC, Admin_ID) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, user.getNIC());
                    stmt.setString(2, adminId);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    public static void deleteUser(String nic) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            String sql = "DELETE FROM Admin WHERE NIC = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nic);
                stmt.executeUpdate();
            }

            sql = "DELETE FROM User WHERE NIC = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nic);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }
}