package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class lec_Materials extends JFrame {
    private JTextField txt2;
    private JButton browsebtn;
    private JTextField txt1;
    private JButton updatebtn;
    private JButton clearbtn;
    private JComboBox comboBox1;
    private JPanel Main_p;

    public lec_Materials() {
        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setContentPane(Main_p);
        setVisible(true);

        updatebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        clearbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        new lec_Materials();
    }
}
