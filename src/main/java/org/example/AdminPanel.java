package org.example;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;

    public AdminPanel(User user) {
        setTitle("Admin Dashboard");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        welcomeLabel = new JLabel("Welcome, " + user.getUsername() + " (Admin)");
        mainPanel.add(welcomeLabel);

        setContentPane(mainPanel);
        setVisible(true);
    }
}
