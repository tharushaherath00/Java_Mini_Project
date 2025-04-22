package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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

    public lec_Materials() {
        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setContentPane(Main_p);
        setVisible(true);

        backbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Dash dash = new lec_Dash();
                dash.setVisible(true);
                dispose();
            }
        });

        browsebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    txt1.setText(selectedFile.getAbsolutePath());
                }
            }
        });


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = comboBox1.getSelectedItem().toString();  // Course_ID
                String filename = txt1.getText();                          // File path input
                File file = new File(filename);

                String driver = "com.mysql.cj.jdbc.Driver";
                String url = "jdbc:mysql://localhost:3306/tec_lms";
                String user = "root";
                String password = "Kali00@#12";



                String savePath = "C:/Users/tharu/Desktop/Lec_Materials/" + selected;


                try {
                    // Save to Database

                    Class.forName(driver);
                    Connection con = DriverManager.getConnection(url, user, password);
                    PreparedStatement ps = con.prepareStatement("UPDATE course SET Course_Materials = ? WHERE Course_ID = ?");
                    FileInputStream fis = new FileInputStream(file);
                    ps.setBinaryStream(1, fis, (int) file.length());
                    ps.setString(2, selected);
                    int result = ps.executeUpdate();
                    ps.close();
                    fis.close();


                    FileInputStream inputStream = new FileInputStream(file);
                    FileOutputStream outputStream = new FileOutputStream(savePath);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                    outputStream.close();

                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "Material updated and saved to Desktop.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Course not found. Nothing updated.");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Upload failed.");
                }
            }
            });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txt1.setText("");
                txt2.setText("");
            }
        });

    }

    public static void main(String[] args) {
        new lec_Materials();
    }
}
