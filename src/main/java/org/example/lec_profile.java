package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class lec_profile  extends JFrame {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton Update;
    private JButton resetButton;
    private JPanel Main_p;

    public lec_profile() {

        setTitle("Lecture Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setContentPane(Main_p);
        setVisible(true);


        Update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        new lec_profile();
    }
}
