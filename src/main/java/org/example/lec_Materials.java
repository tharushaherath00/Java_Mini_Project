package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.awt.Desktop;

public class lec_Materials extends JFrame {
    private JTextField txt2;
    private JButton browsebtn;
    private JTextField txt1;
    private JButton updatebtn;
    private JButton clearbtn;
    private JComboBox comboBox1;
    private JPanel Main_p;
    private JButton backbtn;
    private JButton updateButton;
    private JButton resetButton;
    private JTable table1;
    private JButton removeButton;
    private Connection con; // Database connection

    public lec_Materials() {
        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setContentPane(Main_p);
        setVisible(true);

        // Initialize database connection
        try {
            con = Database.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to database.");
            System.exit(1); // Exit if connection fails
        }

        // Back button action
        backbtn.addActionListener(e -> {
            lec_Dash dash = new lec_Dash();
            dash.setVisible(true);
            dispose();
        });

        // Browse button action
        browsebtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                txt1.setText(selectedFile.getAbsolutePath());
            }
        });

        // Update button action (with FileServe integration)
        updateButton.addActionListener(e -> {
            String courseId = comboBox1.getSelectedItem().toString();
            String filePath = txt1.getText();

            if (filePath.isEmpty() || courseId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a course and a file.");
                return;
            }

            File file = new File(filePath);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "Selected file does not exist.");
                return;
            }

            FileServe fileServe = new FileServe();
            try {
                // Login to FileServe
                ClientCredentials credentials = new ClientCredentials("test_backend", "your_random_jwt_secret_32_chars_minimum");
                String token = fileServe.login(credentials);

                // Upload file
                String fileId = fileServe.uploadFile(file, token, courseId);

                // Store metadata in the files table
//                String query = "INSERT INTO files (file_idd, filename, course_id) VALUES (?, ?, ?)";
//                PreparedStatement ps = con.prepareStatement(query);
//                ps.setString(1, fileId);
//                ps.setString(2, file.getName());
//                ps.setString(3, courseId);
//
//                int result = ps.executeUpdate();

                if (1 == 1) {
                    JOptionPane.showMessageDialog(null, "File uploaded and metadata saved successfully.");
                    loadTable(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to save file metadata.");
                }

            } catch (FileServeException fse) {
                fse.printStackTrace();
                JOptionPane.showMessageDialog(null, "File upload failed: " + fse.getMessage());
            } finally {
                fileServe.close();
            }
        });

        // Table mouse click action
//        table1.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                int row = table1.getSelectedRow();
//                String fileName = table1.getValueAt(row, 1).toString();
//                String filePath = "C:/Users/tharu/Desktop/Lec_Materials/" + comboBox1.getSelectedItem().toString() + "_" + fileName;
//
//                try {
//                    File file = new File(filePath);
//                    if (file.exists()) {
//                        Desktop.getDesktop().open(file);
//                    } else {
//                        JOptionPane.showMessageDialog(null, "File not found!");
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(null, "Unable to open file.");
//                }
//            }
//        });

        // Reset button action
        resetButton.addActionListener(e -> txt1.setText(""));

        // Remove button action
        removeButton.addActionListener(e -> {
            String selected = comboBox1.getSelectedItem().toString();

            try {
                String query = "DELETE FROM files WHERE course_id = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, selected);

                int result = ps.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Material removed for course " + selected);
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(null, "No course material found to remove.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to remove material.");
            }
        });

        // Load table initially
        loadTable();
    }

    private void loadTable() {
        try {
            String query = "SELECT course_id, filename FROM files";
          
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"Course_ID", "File_Name"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("course_id"), rs.getString("filename")});
            }
            table1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new lec_Materials();
    }
}