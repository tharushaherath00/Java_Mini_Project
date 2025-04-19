package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class lec_Marks  extends JFrame {
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTable table1;
    private JButton submitbtn;
    private JButton resetbtn;
    private JPanel Main_P;


    public lec_Marks() {

        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setContentPane(Main_P);
        setVisible(true);

        submitbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        resetbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        new lec_Marks();
    }
}
