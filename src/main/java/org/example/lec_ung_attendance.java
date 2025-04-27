package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class lec_ung_attendance extends JFrame {
    private JPanel Main_P;
    private JButton viewButton;
    private JTable table1;
    private JButton backbtn;
    private JComboBox comboBox1;
    private MyDBConnecter mdc;
    private Connection con;

    public lec_ung_attendance() {

        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        setContentPane(Main_P);
        setVisible(true);

        mdc = new MyDBConnecter();
        con = mdc.getMyConnection();

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String index=comboBox1.getSelectedItem().toString();

                if(index.equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter the index");
                    return;
                }


                try{
                    String sql = "select Course_Name,Days_Present,Days_Medical,Attendance_Percentage from courseattendancesummary  where Course_ID=?";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setString(1, index);
                    ResultSet rs = stmt.executeQuery();

                    DefaultTableModel model=(DefaultTableModel)table1.getModel();
                    model.setRowCount(0);

                    model.setColumnIdentifiers(new String[]{"Course_Name", "Days_Present", "Days_Medical", "Attendance_Percentage"});

                    while (rs.next()) {
                        String Course_Name = rs.getString(1);
                        String Days_Present = rs.getString(2);
                        String Days_Medical = rs.getString(3);
                        String Attendance_Percentage = rs.getString(4);
                        model.addRow(new Object[]{Course_Name,Days_Present,Days_Medical,Attendance_Percentage});
                    }
                    stmt.close();
                    rs.close();


                }
                catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
        });

        backbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Stu dash2 = new lec_Stu();
                dash2.setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new lec_ung_attendance();
    }
}
