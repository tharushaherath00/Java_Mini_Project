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

    public lec_Materials() {
        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setContentPane(Main_p);
        setVisible(true);

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/tec_lms";
        String user = "root";
        String password = "Kali00@#12";

        backbtn.addActionListener(e -> {
            lec_Dash dash = new lec_Dash();
            dash.setVisible(true);
            dispose();
        });

        browsebtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                txt1.setText(selectedFile.getAbsolutePath());
            }
        });

        updateButton.addActionListener(e -> {
            String selected = comboBox1.getSelectedItem().toString();
            String filename = txt1.getText();
            File file = new File(filename);


            String savePath = "C:/Users/tharu/Desktop/Lec_Materials/" + selected + "_" + file.getName();

            try {
                Class.forName(driver);
                Connection con = DriverManager.getConnection(url, user, password);

                PreparedStatement ps = con.prepareStatement("UPDATE course SET Course_Materials = ?, File_Name = ? WHERE Course_ID = ?");
                FileInputStream fis = new FileInputStream(file);
                ps.setBinaryStream(1, fis, (int) file.length());
                ps.setString(2, file.getName());
                ps.setString(3, selected);

                int result = ps.executeUpdate();


                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Material updated ");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Course not found.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Upload failed.");
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                String fileName = table1.getValueAt(row, 1).toString();
                String filePath = "C:/Users/tharu/Desktop/Lec_Materials/" + comboBox1.getSelectedItem().toString() + "_" + fileName;

                try {
                    File file = new File(filePath);
                    if (file.exists()) {
                        Desktop.getDesktop().open(file);
                    } else {
                        JOptionPane.showMessageDialog(null, "File not found!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Unable to open file.");
                }
            }
        });

        resetButton.addActionListener(e -> txt1.setText(""));

        loadTable();
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = comboBox1.getSelectedItem().toString();



                try {
                    Class.forName(driver);
                    Connection con = DriverManager.getConnection(url, user, password);

                    String query = "UPDATE course SET Course_Materials = NULL WHERE Course_ID = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, selected);

                    int result = ps.executeUpdate();

                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "Material removed for course " + selected);
                        loadTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "No course material found to remove.");
                    }

                    ps.close();
                    con.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to remove material.");
                }
            }
        });

    }

    private void loadTable() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/tec_lms";
        String user = "root";
        String password = "Kali00@#12";

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, password);
            String query = "SELECT Course_ID, File_Name FROM course WHERE Course_Materials IS NOT NULL AND File_Name IS NOT NULL";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"Course_ID", "File_Name"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("Course_ID"), rs.getString("File_Name")});
            }
            table1.setModel(model);

            con.close();
            ps.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new lec_Materials();
    }
}
