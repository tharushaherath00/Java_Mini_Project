package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.sql.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UndergraduateViewProfile extends JFrame {

    private String userId;
    private JLabel profilePicLabel;
    private JLabel nameValue, emailValue, dobValue, roleValue;
    private JButton editPicButton;

    private String name, email, dob, role, profilePicPath;

    public UndergraduateViewProfile(String userId) {
        this.userId = userId;
        fetchProfileData();

        setTitle("My Profile");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(new Color(250, 255, 250));
        setContentPane(contentPane);

        JLabel title = new JLabel("Undergraduate Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(55, 80, 55));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title, BorderLayout.NORTH);

        JPanel profilePanel = new JPanel(null);
        profilePanel.setPreferredSize(new Dimension(760, 380));
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createLineBorder(new Color(180, 220, 180)));


        profilePicLabel = new JLabel();
        profilePicLabel.setBounds(540, 40, 160, 160);
        profilePicLabel.setBorder(BorderFactory.createLineBorder(new Color(120, 160, 120)));
        loadImage(profilePicPath);
        profilePanel.add(profilePicLabel);

        editPicButton = new JButton("Edit Picture");
        editPicButton.setBounds(550, 210, 140, 30);
        editPicButton.setFocusPainted(false);
        editPicButton.setBackground(new Color(120, 180, 120));
        editPicButton.setForeground(Color.WHITE);
        profilePanel.add(editPicButton);

        editPicButton.addActionListener(e -> changeProfilePicture());

        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color labelColor = new Color(55, 80, 55);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 30);
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(labelColor);
        profilePanel.add(nameLabel);

        nameValue = new JLabel(name);
        nameValue.setBounds(160, 50, 300, 30);
        nameValue.setFont(valueFont);
        profilePanel.add(nameValue);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 100, 100, 30);
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(labelColor);
        profilePanel.add(emailLabel);

        emailValue = new JLabel(email);
        emailValue.setBounds(160, 100, 300, 30);
        emailValue.setFont(valueFont);
        profilePanel.add(emailValue);

        JLabel dobLabel = new JLabel("DOB:");
        dobLabel.setBounds(50, 150, 100, 30);
        dobLabel.setFont(labelFont);
        dobLabel.setForeground(labelColor);
        profilePanel.add(dobLabel);

        dobValue = new JLabel(dob);
        dobValue.setBounds(160, 150, 300, 30);
        dobValue.setFont(valueFont);
        profilePanel.add(dobValue);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 200, 100, 30);
        roleLabel.setFont(labelFont);
        roleLabel.setForeground(labelColor);
        profilePanel.add(roleLabel);

        roleValue = new JLabel(role);
        roleValue.setBounds(160, 200, 300, 30);
        roleValue.setFont(valueFont);
        profilePanel.add(roleValue);


        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBounds(50, 280, 200, 30);
        backButton.setFocusPainted(false);
        backButton.setBackground(new Color(180, 100, 100));
        backButton.setForeground(Color.WHITE);

        backButton.addActionListener(e -> {
            new UndergraduateDashboard(userId);
            dispose();
        });

        profilePanel.add(backButton);

        contentPane.add(profilePanel, BorderLayout.CENTER);
    }

    private void loadImage(String path) {
        if (path == null || path.isEmpty()) {
            path = "C:\\Users\\ROG\\OneDrive\\Pictures\\Saved Pictures\\default.jpeg";
        }

        File file = new File(path);
        if (!file.exists()) {
            path = "C:\\Users\\ROG\\OneDrive\\Pictures\\Saved Pictures\\default.jpeg";
        }

        ImageIcon imgIcon = new ImageIcon(path);
        Image img = imgIcon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        profilePicLabel.setIcon(new ImageIcon(img));
    }

    private void fetchProfileData() {
        try (Connection con = Database.getConnection()) {
            String sql = "SELECT u.Name, u.Email, u.DOB, u.Role, u.profilePic " +
                    "FROM User u JOIN Student s ON u.NIC = s.NIC WHERE s.Student_ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                name = rs.getString("Name");
                email = rs.getString("Email");
                dob = rs.getString("Dob");
                role = rs.getString("Role");
                profilePicPath = rs.getString("profilePic");

                System.out.println("Profile Picture Path: " + profilePicPath);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading profile:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeProfilePicture() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select New Profile Picture");
        chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String newPath = selectedFile.getAbsolutePath();


            try (Connection con = Database.getConnection()) {
                String update = "UPDATE User SET profilePic = ? WHERE NIC = (SELECT NIC FROM Student WHERE Student_ID = ?)";
                PreparedStatement ps = con.prepareStatement(update);
                ps.setString(1, newPath);
                ps.setString(2, userId);
                ps.executeUpdate();

                loadImage(newPath);
                JOptionPane.showMessageDialog(this, "Profile picture updated successfully!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to update picture:\n" + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
