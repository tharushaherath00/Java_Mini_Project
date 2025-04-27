package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateTOProfile {
    private JPanel main;
    private JPanel buttonback;
    private JTextField name;
    private JTextField nic;
    private JTextField department;
    private JTextField rolefield;
    private JTextField dob;
    private JTextField toid;
    private JTextField DepId;
    private JLabel Name;
    private JButton updateProfileDetailsButton;
    private JLabel title;
    private JButton mainMenuButton;
    private JPanel topic;
    private JLabel niclb;
    private JLabel Birth;
    private JLabel rolelb;

    private final User user;
    private Connection conn;

    public UpdateTOProfile(User user) {
        this.user = user;

        try {
            conn = Database.getConnection();
            loadProfileData(user.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection error.");
        }

        updateProfileDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }
        });

        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Technical Officer Profile");
                profileView profile = new profileView(user);
                frame.setContentPane(profile.getMainPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

                SwingUtilities.getWindowAncestor(main).dispose(); // Close current window
            }
        });
    }

    private void loadProfileData(String email) {
        String query = "SELECT u.Name, u.NIC, u.Email, u.DOB, u.Department_ID, u.Department, u.Role, t.TO_ID, t.Dep_ID " +
                "FROM User u JOIN Technical_Officer t ON u.NIC = t.NIC WHERE u.Email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                name.setText(rs.getString("Name"));
                nic.setText(rs.getString("NIC"));
                department.setText(rs.getString("Department"));
                dob.setText(rs.getString("DOB"));
                rolefield.setText(rs.getString("Role"));
                toid.setText(rs.getString("TO_ID"));
                DepId.setText(rs.getString("Dep_ID"));
            } else {
                JOptionPane.showMessageDialog(null, "Profile not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProfile() {
        String updatedName = name.getText();
        String updatedNIC = nic.getText();
        String updatedDOB = dob.getText();
        String updatedDepartment = department.getText();
        String updatedRole = rolefield.getText();
        String updatedTOID = toid.getText();
        String updatedDepID = DepId.getText();

        String updateUserQuery = "UPDATE User SET Name = ?, DOB = ?, Department = ?, Role = ? WHERE NIC = ?";
        String updateTOQuery = "UPDATE Technical_Officer SET TO_ID = ?, Dep_ID = ? WHERE NIC = ?";

        try (
                PreparedStatement userStmt = conn.prepareStatement(updateUserQuery);
                PreparedStatement toStmt = conn.prepareStatement(updateTOQuery)
        ) {
            userStmt.setString(1, updatedName);
            userStmt.setString(2, updatedDOB);
            userStmt.setString(3, updatedDepartment);
            userStmt.setString(4, updatedRole);
            userStmt.setString(5, updatedNIC);
            userStmt.executeUpdate();

            toStmt.setString(1, updatedTOID);
            toStmt.setString(2, updatedDepID);
            toStmt.setString(3, updatedNIC);
            toStmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Profile updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage());
        }
    }

    public JPanel getMainPanel() {
        return main;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User("tofficer@example.com", Role.TECHNICAL_OFFICER);
            UpdateTOProfile updateForm = new UpdateTOProfile(testUser);
            JFrame frame = new JFrame("Update Technical Officer Profile");
            frame.setContentPane(updateForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
