package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ViewMedical extends JFrame {
    private JPanel main;
    private JTextField studentid, courseid, medicalid, dateField, reasonField;
    private JComboBox<String> status;
    private JButton addMedicalButton, updateMedicalStatusButton, deleteMedicalStatusButton, clearButton;
    private JTable medicalTable;
    private JButton mainMenuButton;
    private JPanel topic;
    private JPanel MedicalView;
    private JPanel buttons;
    private JPanel last;

    public ViewMedical() {
        setTitle("Maintain Medical");
        setSize(600,500);
        setContentPane(main);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        // Button to add a new medical record
        addMedicalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMedicalRecord();
            }
        });

        // Button to update the status of a selected medical record
        updateMedicalStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMedicalStatus();
            }
        });

        // Button to delete a selected medical record
        deleteMedicalStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMedicalStatus();
            }
        });

        // Button to clear all input fields
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        // Button to return to the main menu (closes the window)
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
                if (frame != null) {
                    frame.dispose();
                }
            }
        });

        // Load all medical data into the table on form load
        loadMedicalData();

        // Add selection listener to load data into fields when selecting a row in the table
        medicalTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && medicalTable.getSelectedRow() != -1) {
                    loadSelectedRowData();
                }
            }
        });
    }

    // Method to load all records from the Medical table
    private void loadMedicalData() {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Medical")) {

            String[][] tableData = new String[100][6]; // Increase rows if needed
            int row = 0;

            while (rs.next()) {
                tableData[row][0] = rs.getString("Medical_ID");
                tableData[row][1] = rs.getString("Student_ID");
                tableData[row][2] = rs.getString("Course_ID");
                tableData[row][3] = rs.getString("Date");
                tableData[row][4] = rs.getString("Reason");
                tableData[row][5] = rs.getString("Status");
                row++;
            }

            String[] columnNames = {"Medical ID", "Student ID", "Course ID", "Date", "Reason", "Status"};
            medicalTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to load medical records.");
        }
    }

    // Add a new medical record to the database
    private void addMedicalRecord() {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Medical (Student_ID, Course_ID, Date, Reason, Status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, studentid.getText());
            ps.setString(2, courseid.getText());
            ps.setString(3, dateField.getText());
            ps.setString(4, reasonField.getText());
            ps.setString(5, (String) status.getSelectedItem());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Medical record added.");
            loadMedicalData();
            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding medical record.");
        }
    }

    // Update the status of an existing medical record
    private void updateMedicalStatus() {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE Medical SET Status=? WHERE Medical_ID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, (String) status.getSelectedItem());
            ps.setString(2, medicalid.getText());

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Medical status updated.");
                loadMedicalData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "No record found with that ID.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating medical status.");
        }
    }

    // Delete a medical record from the database
    private void deleteMedicalStatus() {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM Medical WHERE Medical_ID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, medicalid.getText());

            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                JOptionPane.showMessageDialog(null, "Medical record deleted.");
                loadMedicalData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "No record found with that ID.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting medical record.");
        }
    }

    // Clear all input fields
    private void clearFields() {
        studentid.setText("");
        courseid.setText("");
        medicalid.setText("");
        dateField.setText("");
        reasonField.setText("");
        status.setSelectedIndex(0);
        medicalTable.clearSelection();
    }

    // Load the selected row's data into the fields
    private void loadSelectedRowData() {
        int selectedRow = medicalTable.getSelectedRow();
        medicalid.setText(medicalTable.getValueAt(selectedRow, 0).toString());
        studentid.setText(medicalTable.getValueAt(selectedRow, 1).toString());
        courseid.setText(medicalTable.getValueAt(selectedRow, 2).toString());
        dateField.setText(medicalTable.getValueAt(selectedRow, 3).toString());
        reasonField.setText(medicalTable.getValueAt(selectedRow, 4).toString());
        status.setSelectedItem(medicalTable.getValueAt(selectedRow, 5).toString());
    }

    // Allow other classes to get the main panel for display
    public JPanel getMainPanel() {
        return main;
    }
}
