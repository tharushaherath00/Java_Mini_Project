package org.example;
import java.awt.*;
import java.sql.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class lec_ung_details extends JFrame {
    private JTextField textField1;
    private JButton searchButton;
    private JTable table1;
    private JPanel Main_p;
    private JButton backbtn;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private JLabel l4;
    private JLabel l5;
    private JLabel l6;
    private JLabel l7;


    public lec_ung_details() {

        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        setContentPane(Main_p);
        setVisible(true);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             String index = textField1.getText();

             if(index.equals("")){
                 JOptionPane.showMessageDialog(null, "Please enter the index");
                 return;
             }

                String driver = "com.mysql.cj.jdbc.Driver";;
                String url = "jdbc:mysql://localhost:3306/tec_lms";
                String user = "root";
                String password = "Kali00@#12";



                try {
                    Class.forName(driver);
                    Connection conn = DriverManager.getConnection(url, user, password);

                    String sql = "SELECT * FROM `user` WHERE NIC = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, index);
                    ResultSet rs = stmt.executeQuery();


                    if (rs.next()) {
                        l1.setText("NIC: " + rs.getString(1));
                        l2.setText("F_Name: " + rs.getString(2));
                        l3.setText("L_Name: " + rs.getString(3));
                        l4.setText("Address: " + rs.getString(4));
                        l5.setText("E_Mail: " + rs.getString(5));
                        l6.setText("Contact_Num: " + rs.getString(6));
                        l7.setText("DoB: " + rs.getString(7));
                    } else {
                        JOptionPane.showMessageDialog(null, "No user found!");
                    }

                    stmt.close();
                    conn.close();


                }
                catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }

            }
        });

        backbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Stu dash2 = new lec_Stu();
                dash2.setVisible(true);
                dispose();
            }
        });

    }

}
