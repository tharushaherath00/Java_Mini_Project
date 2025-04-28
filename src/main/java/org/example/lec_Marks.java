package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

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
    private JButton refereshButton;
    private MyDBConnecter mdc;
    private Connection con;
    private PreparedStatement ps;





    public lec_Marks() {



        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 600);
        setContentPane(Main_P);
        setVisible(true);

        mdc = new MyDBConnecter();
        con = mdc.getMyConnection();


        viewbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String subject = comboBox1.getSelectedItem().toString();

                try {


                    String sql = "Select Stu_id  from marks where Course_code=?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, subject);
                    ResultSet rs = ps.executeQuery();

                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);
                    model.setColumnIdentifiers(new String[]{"Stu_id", "CA_Status"});

                    while (rs.next()) {
                         String val=rs.getString("Stu_id");
                        model.addRow(new String[]{val});
                    }



                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });


        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int row = table1.getSelectedRow();

                if (row > 0) {
                    String id = table1.getValueAt(row, 0).toString();
                    textField1.setText(id);
                }

            }
        });

        submitbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input1 = textField1.getText();
                String input2 = textField2.getText();
                String subject = comboBox1.getSelectedItem().toString();
                String content = comboBox2.getSelectedItem().toString();

                if (input1.isEmpty()
                        || input2.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select student and Enter Marks Values");
                    return;
                }
                 double Value=Double.parseDouble(textField2.getText());
                if (!(Value >= 0 && Value<= 100)) {
                    JOptionPane.showMessageDialog(null, "Please Eneter a Valid Mark ");
                    return;
                }


                try {
                    String sql = "UPDATE marks SET " + content + " = ? WHERE Stu_id = ? AND Course_code = ?";

                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, input2);
                    ps.setString(2, input1);
                    ps.setString(3, subject);

                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(null, "Marks Updated Successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Marks Updated Failed");
                    }



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


        refereshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subject = comboBox1.getSelectedItem().toString();
                calculateCAMarksAndStatus(subject);
            }
        });
    }
    public void calculateCAMarksAndStatus(String courseCode) {
        try {
            String sql = "SELECT * FROM marks WHERE Course_code = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            model.setColumnIdentifiers(new String[]{"Stu_id", "CA_Status"});

            while (rs.next()) {
                String stuId = rs.getString("Stu_id");

                double[] quizzes = {
                        rs.getDouble("Quiz_01"),
                        rs.getDouble("Quiz_02"),
                        rs.getDouble("Quiz_03"),
                        rs.getDouble("Quiz_04")
                };
                double[] assignments = {
                        rs.getDouble("Assignment_01"),
                        rs.getDouble("Assignment_02")
                };

                Arrays.sort(quizzes);
                Arrays.sort(assignments);

                double caMark = 0;
                double quizAvg = 0, assAvg = 0, mid = 0, mid1 = 0,requiredPercentage=0;

                switch (courseCode) {
                    case "ICT2113":
                        quizAvg = (quizzes[2] + quizzes[3]) / 2;
                        mid = rs.getDouble("Mid_theory");
                        mid1 = rs.getDouble("Mid_practical");
                        caMark = quizAvg * 0.1 + ((mid + mid1) * 0.1);
                        requiredPercentage =caMark*10/3 ;
                        break;
                    //Ca calculating in 30

                    case "ICT2122":
                        quizAvg = (quizzes[1] + quizzes[2] + quizzes[3]) / 3;
                        assAvg = assignments[0];
                        mid = rs.getDouble("Mid_theory");
                        mid1 = rs.getDouble("Mid_practical");
                        caMark = (quizAvg * 0.1) + (assAvg * 0.1) + ((mid + mid1) * 0.1);
                        requiredPercentage =caMark*10/4 ;
                        break;
                    //CA cal in 40

                    case "ICT2133":
                        quizAvg = (quizzes[2] + quizzes[3]) / 2;
                        assAvg = (assignments[0] + assignments[1]);
                        caMark = (quizAvg * 0.1) + (assAvg * 0.1);
                        requiredPercentage =caMark*10/3 ;
                        break;
                    //CA Cal in 30

                    case "ICT2142":
                        assAvg = assignments[0] * 2;
                        mid = rs.getDouble("Mid_theory");
                        mid1 = rs.getDouble("Mid_practical");
                        caMark = (assAvg * 0.1) * ((mid + mid1) * 0.1);
                        requiredPercentage =caMark*10/4 ;
                        break;
                    //CA CAl in 40

                    case "ICT2152":
                        quizAvg = (quizzes[2] + quizzes[3]) / 2;
                        assAvg = (assignments[0] + assignments[1]);
                        caMark = (quizAvg * 0.1) + (assAvg * 0.1);
                        requiredPercentage =caMark*10/3 ;
                        break;
                    //CA Cal in 30

                    default:
                        JOptionPane.showMessageDialog(null, "Invalid course code.");
                        return;
                }

                String status = (requiredPercentage>=50) ? "Pass" : "Fail";
                model.addRow(new String[]{stuId, status});
                String updateQuery = "UPDATE marks SET CA_Status = ? WHERE Course_code = ? AND Stu_id = ?";
                PreparedStatement updatePs = con.prepareStatement(updateQuery);
                updatePs.setString(1, status);
                updatePs.setString(2, courseCode);
                updatePs.setString(3, stuId);
                updatePs.executeUpdate();
                updatePs.close();


            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading CA data.");
        }
    }

}