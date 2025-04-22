package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class lec_ung_gpa extends JFrame {
    private JTextField textField1;
    private JButton searchButton;
    private JTable table1;
    private JPanel Main_P;
    private JButton backbtn;

    public lec_ung_gpa() {

        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        setContentPane(Main_P);
        setVisible(true);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String index=textField1.getText().trim();

                if(index.equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter the index");
                    return;
                }


                String driver = "com.mysql.cj.jdbc.Driver";;
                String url = "jdbc:mysql://localhost:3306/tec_lms";
                String user = "root";
                String password = "Kali00@#12";


                try{
                    Class.forName(driver);
                    Connection conn = DriverManager.getConnection(url, user, password);
                    String sql = "select  Course_ID,Course_Name,Grade,Credits,SGPA from `studentperformanceview`  where Student_ID=?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, index);

                    ResultSet rs = stmt.executeQuery();
                    DefaultTableModel model=(DefaultTableModel)table1.getModel();
                    model.setRowCount(0);

                    model.setColumnIdentifiers(new String[]{"Course_ID", "Course_Name", "Grade", "Credits", "SGPA"});

                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getString("Course_ID"),
                                rs.getString("Course_Name"),
                                rs.getString("Grade"),
                                rs.getString("Credits"),
                                rs.getString("SGPA")
                        });
                    }
                    stmt.close();
                    conn.close();
                    rs.close();


                }
                catch (Exception ex){
                    System.out.println(ex.getMessage());
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
        new lec_ung_gpa();
    }
}
