package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class ViewNotice extends JFrame {
    private JTable table1;
    private JButton mainMenuButton;
    private JPanel MainP;
    public ViewNotice() {
        setTitle("View Notices");
        setSize(600,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(MainP);
        setVisible(true);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"notice_id", "title", "content", "posted_date", "target_role"});
        table1.setModel(model);


        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/tec_lms";
        String user = "root";
        String password = "root";
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, user, password);

            String query = "SELECT * FROM notices";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            model.setRowCount(0); // Clear previous data

            table1.setRowHeight(25);

            while (rs.next()) {
                String notice_id = rs.getString("notice_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String posted_date = rs.getString("posted_date");
                String target_role = rs.getString("target_role");
                model.addRow(new String[]{notice_id, title, content, posted_date, target_role});
            }

            con.close();
            rs.close();
            ps.close();

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
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
      new ViewNotice();
    }

}
