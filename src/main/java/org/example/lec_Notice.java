package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class lec_Notice extends JFrame {
    private JTable table1;
    private JPanel Main_P;
    private JButton backbtn;
    private MyDBConnecter mdc;
    private Connection con;

    public lec_Notice() {
        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setContentPane(Main_P);
        setVisible(true);

        mdc = new MyDBConnecter();
        con = mdc.getMyConnection();
        backbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Dash dash = new lec_Dash();
                dash.setVisible(true);
                dispose();
            }
        });


        try {

            String query = "SELECT * FROM notices";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            model.setColumnIdentifiers(new String[]{"notice_id", "title", "content", "posted_date", "target_role"});



            table1.setRowHeight(25);

            while (rs.next()) {
                String notice_id = rs.getString("notice_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String posted_date = rs.getString("posted_date");
                String target_role = rs.getString("target_role");
                model.addRow(new String[]{notice_id, title, content, posted_date, target_role});
            }



        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row=table1.getSelectedRow();

                String id=table1.getValueAt(row, 0).toString();
                String title=table1.getValueAt(row, 1).toString();
                String content=table1.getValueAt(row, 2).toString();
                String posted_date=table1.getValueAt(row, 3).toString();
                String target_role=table1.getValueAt(row, 4).toString();

                JOptionPane.showMessageDialog(null, "\nID:"+id+" \nTitle:"+title+"\nContent"+content+"\nPost_Date:"+posted_date+"\ntarget_Role:"+target_role,
                        "Notice",JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {

        new lec_Notice();
    }
}
