package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JFrame {
    private JButton userManagement;
    private JButton courseManagement;
    private JButton noticeManagement;
    private JButton timetableManagement;
    private JLabel heading;
    private JPanel main;
    private JLabel welcomeLable;
    private JButton backToLogin;

    private static User user;

    public AdminPanel(){
        setTitle("Admin Dashboard");
        setSize(400, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(main);
        setup();
//        setVisible(true);
//        revalidate();

    }

    public AdminPanel(User user) {
        setTitle("Admin Dashboard");
        setSize(600, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(main);

        AdminPanel.user = user;

        setup();
        setVisible(true);
//      revalidate();


    }

    public User getUser(){
        return user;
    }

    public void setup(){
        welcomeLable.setText("Welcome, "+user.getUsername()+"("+user.getRole()+")");

        courseManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new courseMPUI();
            }
        });
        noticeManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                noticeMUI notice = new noticeMUI();
            }
        });
        backToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose();
            }
        });
        userManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UserMNG(user);
            }
        });
        timetableManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new TimetableMNG(user);
            }
        });
    }



}
