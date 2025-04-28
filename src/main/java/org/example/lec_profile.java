package org.example;
import java.sql.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class lec_profile  extends JFrame {
    private JButton Update;
    private JButton resetButton;
    private JPanel Main_p;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton updateButton;
    private JButton backbtn;
    private JTextField textField2;
    private MyDBConnecter mdc;
    private Connection con;

    public lec_profile() {

        setTitle("Lecture Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setContentPane(Main_p);
        setVisible(true);

        mdc = new MyDBConnecter();
        con = mdc.getMyConnection();

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textField1.getText();
                String pass = String.valueOf(passwordField1.getPassword());
                String index = textField2.getText().trim();


                if (name.isEmpty() || pass.isEmpty() || index.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter all required values.");
                    return;
                }


                try {

                    String query = "UPDATE user SET Name = ?, Password = ? WHERE NIC = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, name);
                    ps.setString(2, pass);
                    ps.setString(3, index);

                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(null, "Lecture Profile Updated");
                    } else {
                        JOptionPane.showMessageDialog(null, "No matching record found for the provided NIC.");
                    }

                    con.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField2.setText("");
               textField1.setText("");
               passwordField1.setText("");
            }
        });
        backbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Dash dash = new lec_Dash();
                dash.setVisible(true);
                dispose();
            }
        });
    }
}


