package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class addNoticeUI extends JFrame {
    private JTextField textField1;
    private JComboBox comboBox1;
    private JTextField textField2;
    private JTextArea textArea1;
    private JLabel heading;
    private JLabel title;
    private JLabel date;
    private JLabel audiance;
    private JLabel messege;
    private JButton publish;
    private JButton back;
    private JPanel addNoticePanel;

    public addNoticeUI() {
        setTitle("add notice");
        setSize(400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(addNoticePanel);
        setVisible(true);
        setLocationRelativeTo(null);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                noticeMUI notice = new noticeMUI();
            }
        });
    }
}
