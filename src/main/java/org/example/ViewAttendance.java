package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewAttendance {

    private JPanel main;
    private JPanel topic;
    private JPanel AttendanceView;
    private JPanel buttons;
    private JButton mainMenuButton;
    private JTable atendancetable;
    private JButton deleteButton;

    private User currentUser;

    public ViewAttendance(User user) {
        this.currentUser = user;

        // Load data into JTable
        loadAttendanceData();

        // Action listener for Main Menu button
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

        // Action listener for Delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = atendancetable.getSelectedRow();
                if (selectedRow != -1) {
                    String attendanceId = (String) atendancetable.getValueAt(selectedRow, 0); // Get the Attendance_ID

                    // Confirm deletion
                    int confirm = JOptionPane.showConfirmDialog(main, "Are you sure you want to delete this attendance?", "Delete Attendance", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteAttendance(attendanceId);
                    }
                } else {
                    JOptionPane.showMessageDialog(main, "Please select a row to delete.");
                }
            }
        });

        // Show this panel in a new frame
        JFrame frame = new JFrame("View Attendance");
        frame.setContentPane(main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void loadAttendanceData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "Attendance_ID", "Student_ID", "Course_ID", "Session_Type", "Status", "Date"
        });

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM atendance")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("Attendance_ID"),
                        rs.getString("Student_ID"),
                        rs.getString("Course_ID"),
                        rs.getString("Session_Type"),
                        rs.getString("Status"),
                        rs.getString("Date")
                });
            }

            atendancetable.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(main, "Error loading attendance: " + e.getMessage());
        }
    }

    private void deleteAttendance(String attendanceId) {
        String sql = "DELETE FROM atendance WHERE Attendance_ID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, attendanceId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(main, "Attendance record deleted successfully.");
                // Reload data after deletion
                loadAttendanceData();
            } else {
                JOptionPane.showMessageDialog(main, "Error: Could not delete attendance record.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(main, "Error: " + e.getMessage());
        }
    }

    private void createUIComponents() {
        // Custom component init if needed (left blank intentionally)
    }

    public JPanel getMainPanel() {
        return main;
    }
}
