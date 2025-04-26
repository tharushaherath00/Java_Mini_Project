package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ViewMedical {
    private JPanel main;
    private JPanel topic;
    private JPanel MedicalView;
    private JPanel buttons;
    private JButton mainMenuButton;
    private JTextField studentid;
    private JTextField courseid;
    private JTextField medicalid;
    private JTextField dateField;
    private JComboBox<String> status;
    private JButton ADDMedicalStatusButton;
    private JButton updateMedicalStatusButton;
    private JButton deleteMedicalStatusButton;
    private JButton downloadMedicalDocumentButton;
    private JPanel last;
    private JTable medicalTable;

    public ViewMedical() {
        loadMedicalData();

        // Select row and load to fields
        medicalTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = medicalTable.getSelectedRow();
                if (row != -1) {
                    medicalid.setText(medicalTable.getValueAt(row, 0).toString());
                    studentid.setText(medicalTable.getValueAt(row, 1).toString());
                    courseid.setText(medicalTable.getValueAt(row, 2).toString());
                    dateField.setText(medicalTable.getValueAt(row, 3).toString());
                    status.setSelectedItem(medicalTable.getValueAt(row, 5).toString());
                }
            }
        });

        updateMedicalStatusButton.addActionListener(e -> updateMedicalStatus());
        deleteMedicalStatusButton.addActionListener(e -> deleteMedicalStatus());
        downloadMedicalDocumentButton.addActionListener(e -> downloadMedicalDocument());
        mainMenuButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
            frame.dispose(); // Close current
        });
    }

    public JPanel getMainPanel() {
        return main;
    }

    private void loadMedicalData() {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Medical_ID, Student_ID, Course_ID, Date, Reason, Status FROM Medical")) {

            ResultSetMetaData metaData = rs.getMetaData();
            Vector<String> columnNames = new Vector<>();
            for (int column = 1; column <= metaData.getColumnCount(); column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            medicalTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load medical records.");
        }
    }

    private void updateMedicalStatus() {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE Medical SET Status=? WHERE Medical_ID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status.getSelectedItem().toString());
            ps.setInt(2, Integer.parseInt(medicalid.getText()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Medical status updated.");
            loadMedicalData();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating medical status.");
        }
    }

    private void deleteMedicalStatus() {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM Medical WHERE Medical_ID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(medicalid.getText()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Medical record deleted.");
            loadMedicalData();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting medical record.");
        }
    }

    private void downloadMedicalDocument() {
        JOptionPane.showMessageDialog(null, "Download functionality not implemented.");
    }
}
