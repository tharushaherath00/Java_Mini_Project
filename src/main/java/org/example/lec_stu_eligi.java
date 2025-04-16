package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class lec_stu_eligi extends JFrame {
    private JTextField textField1;
    private JButton searchButton;
    private JTable table1;
    private JPanel Main_P;

    public lec_stu_eligi() {

        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setContentPane(Main_P);
        setVisible(true);


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        new lec_stu_eligi();
    }
}
