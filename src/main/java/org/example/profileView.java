package org.example;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class profileView {
    private JPanel main;
    private JPanel profile_main;
    private JLabel welcomeTechnicalOfficerProfileLabel;
    private JTextField nameTextField;
    private JTextField email;
    private JTextField dobto;
    private JTextField departmentid;
    private JTextField department;
    private JTextField toid;
    private JTextField depid;
    private JTextField nic;
    private JButton technicalOfficerProfileUpdateButton;
    private JButton maintainMedicalButton;
    private JButton ViewNoticebutton;
    private JButton updateMedicalButton;
    private JButton viewAttendanceButton;
    private JButton addAttendanceButton;
    private JPanel full;
    private JButton viewTimeTableButton;
    private JPanel Buttonon;

    private Connection myconn;

    private void createUIComponents() {
        // Do not delete - required for IntelliJ GUI Designer
    }

    public profileView(User user) {
        try {
            myconn = Database.getConnection();
            loadProfileData(user.getUsername()); // username is email
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection error.");
        }


        maintainMedicalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Maintain Medical Records");
                frame.setContentPane(new ViewMedical().getMainPanel());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // Center the frame
                frame.setVisible(true);
            }
        });




        ViewNoticebutton.addActionListener(e -> openAddMedical());
        updateMedicalButton.addActionListener(e -> openUpdateMedical());
        viewAttendanceButton.addActionListener(e -> openViewAttendance());
        addAttendanceButton.addActionListener(e -> openAddAttendance());

        technicalOfficerProfileUpdateButton.addActionListener(e -> {
            JFrame frame = new JFrame("Update Profile");
            UpdateTOProfile updateForm = new UpdateTOProfile(user);
            frame.setContentPane(updateForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            SwingUtilities.getWindowAncestor(main).dispose(); // close current
        });

        viewTimeTableButton.addActionListener(e -> openViewTimeTable());

        // Show in frame (optional if already handled in caller)
        JFrame frame = new JFrame("Technical Officer Profile");
        frame.setContentPane(main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void loadProfileData(String emailParam) {
        String query = "SELECT u.Name, u.NIC, u.Email, u.DOB, u.Department_ID, u.Department, t.TO_ID, t.Dep_ID " +
                "FROM User u JOIN Technical_Officer t ON u.NIC = t.NIC WHERE u.Email = ?";

        try (PreparedStatement stmt = myconn.prepareStatement(query)) {
            stmt.setString(1, emailParam);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nameTextField.setText(rs.getString("Name"));
                    nic.setText(rs.getString("NIC"));
                    email.setText(rs.getString("Email"));
                    dobto.setText(rs.getString("DOB"));
                    departmentid.setText(rs.getString("Department_ID"));
                    department.setText(rs.getString("Department"));
                    toid.setText(rs.getString("TO_ID"));
                    depid.setText(rs.getString("Dep_ID"));
                } else {
                    JOptionPane.showMessageDialog(null, "No profile found for this user.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading profile: " + ex.getMessage());
        }
    }

    private void openMaintainMedical() {
        JOptionPane.showMessageDialog(null, "Open Maintain Medical Function");
    }

    private void openAddMedical() {
        JOptionPane.showMessageDialog(null, "Open Add Medical Function");
    }

    private void openUpdateMedical() {
        JOptionPane.showMessageDialog(null, "Open Update Medical Function");
    }

    private void openViewAttendance() {
        JOptionPane.showMessageDialog(null, "Open View Attendance Function");
    }

    private void openAddAttendance() {
        JOptionPane.showMessageDialog(null, "Open Add Attendance Function");
    }

    private void openViewTimeTable() {
        JOptionPane.showMessageDialog(null, "Open View Time Table Function");
    }

    private void updateProfile() {
        JOptionPane.showMessageDialog(null, "Update Profile Function (to be implemented)");
    }

    public JPanel getMainPanel() {
        return main;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User("tofficer@example.com", Role.TECHNICAL_OFFICER);
            new profileView(testUser);
        });
    }
}
