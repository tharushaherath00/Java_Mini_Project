package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class lec_Marks  extends JFrame {
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTable table1;
    private JButton submitbtn;
    private JButton resetbtn;
    private JPanel Main_P;
    private JButton backbtn;
    private JTextField textField1;
    private JTextField textField2;
    private JButton viewbtn;


    public lec_Marks() {

        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 600);
        setContentPane(Main_P);
        setVisible(true);

        String driver="com.mysql.cj.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/tec_lms";
        String user="root";
        String password = "Kali00@#12";

        viewbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String subject = comboBox1.getSelectedItem().toString();

                try {
                    Class.forName(driver);
                    Connection con= DriverManager.getConnection(url,user,password);

                    String sql="Select Stu_id  from marks where Course_code=?";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setString(1,subject);
                    ResultSet rs=ps.executeQuery();

                    DefaultTableModel model=(DefaultTableModel) table1.getModel();
                    model.setRowCount(0);
                    model.setColumnIdentifiers(new String[]{"Stu_id","CA_status"});

                    while(rs.next()){
                        String S_id=rs.getString("Stu_id");
                        model.addRow(new String[]{S_id});

                    }


                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });


        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int row=table1.getSelectedRow();

                if (row>0) {
                    String id=table1.getValueAt(row, 0).toString();
                    textField1.setText(id);
                }

            }
        });

        submitbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input1=textField1.getText();
                String input2=textField2.getText();
                String subject = comboBox1.getSelectedItem().toString();
                String content = comboBox2.getSelectedItem().toString();

                if(input1.isEmpty()
                || input2.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Please select student and Enter Marks Values");
                    return;
                }

                try {
                    Class.forName(driver);
                    Connection con= DriverManager.getConnection(url,user,password);
                    String sql = "UPDATE marks SET " + content + " = ? WHERE Stu_id = ? AND Course_code = ?";

                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setString(1, input2);
                    ps.setString(2, input1);
                    ps.setString(3, subject);

                    int rows=ps.executeUpdate();

                    if(rows>0){
                        JOptionPane.showMessageDialog(null,"Marks Updated Successfully");
                    }else{
                        JOptionPane.showMessageDialog(null,"Marks Updated Failed");
                    }
                    con.close();




                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

            }
        });
        resetbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              textField1.setText("");
              textField2.setText("");
            }
        });
        backbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Dash dash = new lec_Dash();
                dash.setVisible(true);
                dispose();
            }
        });


    }

    public static void main(String[] args) {
        new lec_Marks();
    }
}
