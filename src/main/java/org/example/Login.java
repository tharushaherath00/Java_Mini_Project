package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static String getStudentID() {
        return StudentID;
    }

//    public static void setStudentID(String studentID) {
//        StudentID = studentID;
//    }

    private JButton loginButton;

    private static String StudentID;

    public Login() {
        setTitle("User Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        setVisible(true);
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        StudentID = username;
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            return;
        }

        String query = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Role role = Role.fromString(rs.getString("role"));
                JOptionPane.showMessageDialog(this, "Login Successful!");

                openDashboard(new User(username, role));
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    private void openDashboard(User user) {
        switch (user.getRole()) {
            case ADMIN -> new AdminPanel(user);
            case STUDENT -> new StudentPanel(user);
            default -> JOptionPane.showMessageDialog(this, "Role not implemented.");
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(Login::new);
    }
}
