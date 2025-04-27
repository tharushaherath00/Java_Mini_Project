package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private String studentId;

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
//        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
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

        String query = "SELECT u.Password, u.Role, s.Student_ID " +
                "FROM User u " +
                "LEFT JOIN Student s ON u.NIC = s.NIC " +
                "WHERE u.Email = ?";


        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");

                studentId = rs.getString("Student_ID");
                if (BCrypt.checkpw(password, storedHash)) {
                    Role role = Role.fromString(rs.getString("role"));
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    openDashboard(new User(username, role));
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Credentials!");
                }
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
            case STUDENT -> new UndergraduateDashboard(studentId);
            case TECHNICAL_OFFICER -> new profileView(user);
            case LECTURER -> new lec_Dash();
            default -> JOptionPane.showMessageDialog(this, "Role not implemented.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}