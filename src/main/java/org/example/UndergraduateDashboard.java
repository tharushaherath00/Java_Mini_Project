package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UndergraduateDashboard extends JFrame {
    private JButton profilebtn;
    private JButton coursebtn;
    private JButton attendancebtn;
    private JButton gradebtn;
    private JButton timetablebtn;
    private JButton noticebtn;
    private JPanel Under_Dash;
    private String userId;


    public UndergraduateDashboard(String userId) {
        this.userId = userId;

        setTitle("Undergraduate Dashboard");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(Under_Dash);
        setVisible(true);


        profilebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new UndergraduateViewProfile(userId);
                dispose();
            }
        });
        coursebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UndergraduateViewCourseDetails(userId);
                dispose();

            }
        });
        attendancebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UndergraduateViewAttendance(userId);
                dispose();
            }
        });
        gradebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            new UndergraduateViewGrade(userId);
            dispose();
            }
        });
        timetablebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UndergraduateViewTimeTable(userId);
                dispose();

            }
        });
        noticebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UndergraduateViewNotice(userId);
                dispose();

            }
        });
    }
//    public static void main(String[] args) {
//        new UndergraduateDashboard("S002");
//    }
}
