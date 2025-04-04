package admin;

import javax.swing.*;

public class adminPanel extends JFrame {
    private JButton userMnagementButton;
    private JButton courseManagementButton;
    private JButton noticeManagementButton;
    private JButton timeTableManagementButton;
    private JPanel adminPanel;

    public adminPanel(){
        setTitle("Admin panel");
        setSize(400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setContentPane(adminPanel);
        setLocationRelativeTo(null);
    }
}

