package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JFrame {
    private JLabel welcomeLabel;
    private JButton userManagement;
    private JButton courseManagement;
    private JButton noticeManagement;
    private JButton timetableManagement;
    private JLabel heading;
    private JPanel main;

    public AdminPanel(User user) {
        setTitle("Admin Dashboard");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(main);

        welcomeLabel = new JLabel("Welcome, " + user.getUsername() + " (Admin)");



        setVisible(true);
        courseManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courseMPUI course = new courseMPUI();
            }
        });
    }
}
