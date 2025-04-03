package org.example;

import javax.swing.*;
import java.awt.*;

public class StudentPanel extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;

    public StudentPanel(User user) {
        setTitle("Student Dashboard");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        welcomeLabel = new JLabel("Welcome, " + user.getUsername() + " (Student)");
        mainPanel.add(welcomeLabel);

        setContentPane(mainPanel);
        setVisible(true);
    }
}
