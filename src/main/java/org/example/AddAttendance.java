package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddAttendance {
    private JPanel main;
    private JTextField attendanceid;
    private JTextField studentid;
    private JTextField coursecode;
    private JTextField sessiontype;
    private JTextField status;
    private JTextField date;
    private JButton addAttendanceButton;
    private JButton mainMenuButton;
    private JPanel buttonback;
    private JLabel title;
    private JPanel First;
    private JLabel Name;
    private JLabel sIdlabl;
    private JLabel Codelbl;
    private JLabel statuslbl;
    private JLabel datelbl;
    private JLabel titlelable;

    private User currentUser;

    public AddAttendance(User user) {
        this.currentUser = user;

        // Make attendance ID read-only
        attendanceid.setEditable(false);
        generateAndSetAttendanceId(); // Load next ID when form opens

        // Add Attendance button
        addAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = attendanceid.getText().trim();
                String sid = studentid.getText().trim();
                String code = coursecode.getText().trim();
                String type = sessiontype.getText().trim();
                String stat = status.getText().trim();
                String d = date.getText().trim();

                if (id.isEmpty() || sid.isEmpty() || code.isEmpty() || type.isEmpty() || stat.isEmpty() || d.isEmpty()) {
                    JOptionPane.showMessageDialog(main, "All fields are required.");
                } else {
                    addAtendance(id, sid, code, type, stat, d);
                    generateAndSetAttendanceId(); // Refresh for next entry
                }
            }
        });

        // Main Menu button
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
                if (frame != null) {
                    frame.dispose();
                }
                new profileView(currentUser);
            }
        });
    }

    // Generate and display the next Attendance_ID
    private void generateAndSetAttendanceId() {
        String query = "SELECT MAX(Attendance_ID) AS max_id FROM atendance";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            int nextId = 1;
            if (rs.next()) {
                nextId = rs.getInt("max_id") + 1;
            }

            attendanceid.setText(String.valueOf(nextId));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(main, "Error generating Attendance ID: " + e.getMessage());
        }
    }

    // Insert into database
    private void addAtendance(String id, String studentId, String courseCode, String sessionType, String status, String date) {
        String sql = "INSERT INTO atendance (Attendance_ID, Student_ID, Course_ID, Session_Type, Status, Date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            ps.setString(2, studentId);
            ps.setString(3, courseCode);
            ps.setString(4, sessionType);
            ps.setString(5, status);
            ps.setString(6, date);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(main, "Attendance added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(main, "Error: " + e.getMessage());
        }
    }

    public JPanel getMainPanel() {
        return main;
    }
}
