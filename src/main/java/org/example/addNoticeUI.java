package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;

public class addNoticeUI extends JFrame {
    private JComboBox comboBox1;
    private JTextArea notice;
    private JLabel titleLable;
    private JLabel audianceLable;
    private JLabel noticeLable;
    private JButton publish;
    private JButton back;
    private JPanel addNoticePanel;
    private JTextField titleField;
    private JTextArea noticeField;
    private JButton clear;
//    private JPanel headingPanel;
    private String userName = new AdminPanel().getUser().getUsername();
    private static String userid;

    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;

    public addNoticeUI() {
        setTitle("add notice");
        setSize(600,500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(addNoticePanel);
        setVisible(true);
        setLocationRelativeTo(null);
        comboBox1.addItem("Student");
        comboBox1.addItem("Lecturer");
        comboBox1.addItem("All");

//        System.out.println(new AdminPanel().getUser().getUsername());
//        headingPanel.setSize(600,200);

        getUserId();
        setup();


    }

//    public static void main(String[] args) {
//        new addNoticeUI();
//    }

    public void setup(){
        //back button listner
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                noticeMUI notice = new noticeMUI();
            }
        });

        //publish button listener
        publish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(titleField.getText().isEmpty()||comboBox1.getSelectedItem()==null||noticeField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"All fields need to be filled!..");
                    return;
                }
                else{
                    String title = titleField.getText();
                    String target = comboBox1.getSelectedItem().toString();
                    String notice = noticeField.getText();
                    LocalDateTime now = LocalDateTime.now();
                    Timestamp timestamp = Timestamp.valueOf(now);
                    String query = "INSERT INTO notices(title,content,posted_date,posted_by,target_role)VALUES(?,?,?,?,?)";

                    try{
                        con = Database.getConnection();
                        pstmt = con.prepareStatement(query);
                        pstmt.setString(1,title);
                        pstmt.setString(2,notice);
                        pstmt.setTimestamp(3,timestamp);
                        pstmt.setString(4,userid);
                        pstmt.setString(5,target);
                        int i = pstmt.executeUpdate();
                        if(i>0){
                            JOptionPane.showMessageDialog(null,"Notice published successfully");
                            titleField.setText("");
                            comboBox1.setSelectedItem(null);
                            noticeField.setText("");
                        }


                    }catch(Exception a){
                        JOptionPane.showMessageDialog(null,a.getMessage());
                    }
                }

            }
        });

        //clear button listner
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                titleField.setText("");
                comboBox1.setSelectedItem(null);
                noticeField.setText("");
            }
        });
    }

    public void getUserId() {
        String query = "SELECT * FROM user WHERE Email =?";
        try {
            con = Database.getConnection();
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, userName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                addNoticeUI.userid= rs.getString(1); // Safe: only called if result exists

            } else {
                System.out.println("Invalid User!");

            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());

        }
    }

}
