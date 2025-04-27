package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class lec_Dash extends JFrame {
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;
    private JButton btn4;
    private JButton btn5;
    private JPanel lec_dash;

    public lec_Dash() {

        setTitle("Lecture Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 800);
        setContentPane(lec_dash);
        setVisible(true);


        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_profile l1 = new lec_profile();
                l1.setVisible(true);
                dispose();
            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Materials l2 = new lec_Materials ();
                l2.setVisible(true);
                dispose();
            }
        });
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Marks l3 = new lec_Marks ();
                l3.setVisible(true);
                dispose();
            }
        });
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Stu l4 = new lec_Stu ();
                l4.setVisible(true);
                dispose();
            }
        });
        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Notice l5 = new lec_Notice ();
                l5.setVisible(true);
                dispose();
            }
        });
    }
    public static void main(String[] args) {
        new lec_Dash();
    }
}
