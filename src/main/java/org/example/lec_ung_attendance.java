package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class lec_ung_attendance extends JFrame {
    private JPanel Main_P;
    private JTextField textField1;
    private JButton viewButton;
    private JTable table1;

    public lec_ung_attendance() {

        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setContentPane(Main_P);
        setVisible(true);


        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        new lec_ung_attendance();
    }
}
