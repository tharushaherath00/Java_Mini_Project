package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class lec_stu_eligi extends JFrame {
    private JTextField textField1;
    private JButton searchButton;
    private JPanel Main_P;
    private JButton backbtn;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;

    public lec_stu_eligi() {

        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setContentPane(Main_P);
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

                    String sql = "SELECT  Attendance_Percentage,CA_Marks,Eligibility_Status FROM `EligibilityView` WHERE Student_ID =?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, index);
                    ResultSet rs = stmt.executeQuery();




                    if (rs.next()) {
                        l1.setText("Attendence: " + rs.getString(1));
                        l2.setText("CA_Marks: " + rs.getString(2));
                        l3.setText("Status: " + rs.getString(3));

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

    public static void main(String[] args) {
        new lec_stu_eligi();
    }
}
