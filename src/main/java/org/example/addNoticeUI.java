package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class addNoticeUI extends JFrame {
    private JComboBox comboBox1;
    private JTextArea notice;
    private JLabel heading;
    private JLabel titleLable;
    private JLabel audianceLable;
    private JLabel noticeLable;
    private JButton publish;
    private JButton back;
    private JPanel addNoticePanel;
    private JTextField titleField;
    private JTextArea noticeField;
    Connection con;
    PreparedStatement pstmt;

    public addNoticeUI() {
        setTitle("add notice");
        setSize(400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(addNoticePanel);
        setVisible(true);
        setLocationRelativeTo(null);
        comboBox1.addItem("Student");
        comboBox1.addItem("Lecturer");
//        comboBox1.addItem("Admin");
//        comboBox1.addItem("Dean");
        comboBox1.addItem("All");

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                noticeMUI notice = new noticeMUI();
            }
        });
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
                    String query = "INSERT INTO notices(title,content,posted_date,target_role)VALUES(?,?,?,?)";

                    try{
                        con = Database.getConnection();
                        pstmt = con.prepareStatement(query);
                        pstmt.setString(1,title);
                        pstmt.setString(2,notice);
                        pstmt.setTimestamp(3,timestamp);
                        pstmt.setString(4,target);
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
    }
}
