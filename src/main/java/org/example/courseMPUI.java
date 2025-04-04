package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class courseMPUI extends JFrame {
    private JTable table1;
    private JLabel heading;
    private JPanel main;
    private JButton add;
    private JButton delete;
    private JButton update;
    private JButton back;
    private JPanel inputPanel;
    private JPanel showpanel;
    private JTextField courseId;
    private JTextField courseCredit;
    private JTextField courseName;
    private JRadioButton practical;

    Connection con;
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;



    public courseMPUI() {
        setTitle("Course Management");
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(main);
        setVisible(true);
        createTable();

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Id = courseId.getText();
                String name = courseName.getText();
                String type = "";
                if(practical.isSelected()){
                    type = "Practical";
                }else
                    type = "Theory";
                String credit = courseCredit.getText();

                String query = "INSERT INTO tableNmae(colums) VALUES(?,?,?,?)";
                String checkUser = "SELECT * FROM tableName WHERE courseID = ?";
                if(courseId.equals(null)||courseName.equals(null)||credit.equals(null)||!practical.isSelected()){
                    JOptionPane.showMessageDialog(null,"All fields need to filled");
                    return;
                }
                try{
                    con = Database.getConnection();
                    pstmt =con.prepareStatement(checkUser);
                    pstmt.setString(1,Id);
                    rs = pstmt.executeQuery();
                    if(rs.next()){
                        JOptionPane.showMessageDialog(null,"Course already exist");
                        return;
                    }else{
                        pstmt = con.prepareStatement(query);
                        pstmt.setString(1,Id);
                        pstmt.setString(2,name);
                        pstmt.setString(3,type);
                        pstmt.setString(4,credit);
                        int i = pstmt.executeUpdate();
                        if(i>0) {
                            JOptionPane.showMessageDialog(null, "Course added successfully");
                            courseId.setText("");
                            courseName.setText("");
                            courseCredit.setText("");
                        }
                    }
                }catch(SQLException a){
                    JOptionPane.showMessageDialog(null,a.getMessage());
                }catch(Exception q){
                    JOptionPane.showMessageDialog(null,q.getMessage());
                }
            }
        });
    }
    public void createTable(){
        table1.setModel(new javax.swing.table.DefaultTableModel(null,new String[]{"CourseId","CourseName","CourseCredit","CourseType"}));
    }
}
