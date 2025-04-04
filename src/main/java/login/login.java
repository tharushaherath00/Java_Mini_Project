package login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class login extends JFrame{
    private JTextField txtUName;
    private JTextField txtPass;
    private JButton cancelButton;
    private JButton loginButton;
    private JPanel loginPanel;

    public login() {
        setTitle("Login Page");
        setSize(400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setContentPane(loginPanel);
        setLocationRelativeTo(null);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String UserName = txtUName.getText();
                String Password = txtPass.getText();
                // need to check database users and if user is admin go to the admin panel otherwise go to prefered panel
                if(UserName.equals("john")&&Password.equals("123")){
                    admin.adminPanel adp = new admin.adminPanel();
                    adp.setVisible(true);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(null,"Invalid UserName or Password");
                    txtUName.setText("");
                    txtPass.setText("");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new login()); // Run GUI safely
    }
}
