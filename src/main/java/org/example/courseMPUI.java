package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;

public class courseMPUI extends JFrame {
    private JTable table1;
    private JLabel heading;
    private JPanel main;
    private JButton add;
    private JButton delete;
    private JButton update;
    private JButton back;
    private JPanel inputPanel;
    private JPanel showpanel;
    private JTextField courseId;
    private JTextField courseCredit;
    private JTextField courseName;
    private int selectRow;
    DefaultTableModel dtm;
//    private JRadioButton practical;
    private JComboBox type;

    Connection con;
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;



    public courseMPUI() {
        setTitle("Course Management");
        setSize(400,500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(main);
        setVisible(true);
        type.addItem("Practical");
        type.addItem("Theory");
        createTable();
        tableLoad();

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Id = courseId.getText();
                String name = courseName.getText();
                String Ctype = type.getSelectedItem().toString();
//                for radio button

//                if(practical.isSelected()){
//                    type = "Practical";
//                }else
//                    type = "Theory";
                String credit = courseCredit.getText();

                String query = "INSERT INTO course(c_id,c_name,c_type,c_credit) VALUES(?,?,?,?)";
                String checkUser = "SELECT * FROM course WHERE c_id = ?";
                if(courseId.getText().isEmpty()||courseName.getText().isEmpty()||courseCredit.getText().isEmpty()||type.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null,"All fields need to filled");
                    return;
                }
                try{
                    con = Database.getConnection();
                    pstmt =con.prepareStatement(checkUser);
                    pstmt.setString(1,Id);
                    rs = pstmt.executeQuery();
                    if(rs.next()){
                        JOptionPane.showMessageDialog(null,"Course already exist");
                        return;
                    }else{
                        int Ccredit = Integer.parseInt(credit);
                        pstmt = con.prepareStatement(query);
                        pstmt.setString(1,Id);
                        pstmt.setString(2,name);
                        pstmt.setString(3,type.getSelectedItem().toString());
                        pstmt.setInt(4,Ccredit);
                        int i = pstmt.executeUpdate();
                        if(i>0) {
                            JOptionPane.showMessageDialog(null, "Course added successfully");
                            courseId.setText("");
                            courseName.setText("");
                            courseCredit.setText("");
                            tableLoad();
                        }
                    }
                }catch(SQLException a){
                    JOptionPane.showMessageDialog(null,a.getMessage());
                }catch(Exception q){
                    JOptionPane.showMessageDialog(null,q.getMessage());
                }
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dtm = (DefaultTableModel)table1.getModel();
                selectRow = table1.getSelectedRow();

                String cId = dtm.getValueAt(selectRow,0).toString();
                courseId.setText(cId);
                courseName.setText(dtm.getValueAt(selectRow,1).toString());
                type.setSelectedItem(dtm.getValueAt(selectRow,2));
                courseCredit.setText(dtm.getValueAt(selectRow,3).toString());





            }
        });
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Id = courseId.getText();
                String name = courseName.getText();
                String Ctype = type.getSelectedItem().toString();
//                for radio button

//                if(practical.isSelected()){
//                    type = "Practical";
//                }else
//                    type = "Theory";
                String credit = courseCredit.getText();

                String query = "UPDATE course SET c_name = ?,c_type = ?,c_credit = ? WHERE c_id = ?";
                //String checkUser = "SELECT * FROM course WHERE c_id = ?";
                if(courseId.getText().isEmpty()||courseName.getText().isEmpty()||courseCredit.getText().isEmpty()||type.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null,"All fields need to filled");
                    return;
                }
                try{
                    con = Database.getConnection();
                    //pstmt =con.prepareStatement(checkUser);
                    //pstmt.setString(1,Id);
                    //rs = pstmt.executeQuery();
//                    if(rs.next()){
//                        JOptionPane.showMessageDialog(null,"Course already exist");
//                        return;
//                    }

                        int Ccredit = Integer.parseInt(credit);
                        pstmt = con.prepareStatement(query);
                        pstmt.setString(4,Id);
                        pstmt.setString(1,name);
                        pstmt.setString(2,type.getSelectedItem().toString());
                        pstmt.setInt(3,Ccredit);
                        int i = pstmt.executeUpdate();
                        if(i>0) {
                            JOptionPane.showMessageDialog(null, "Course Updated successfully");
                            courseId.setText("");
                            courseName.setText("");
                            courseCredit.setText("");
                            tableLoad();
                        }

                }catch(SQLException a){
                    JOptionPane.showMessageDialog(null,a.getMessage());
                }catch(Exception q){
                    JOptionPane.showMessageDialog(null,q.getMessage());
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Id = courseId.getText();
                String name = courseName.getText();
                String Ctype = type.getSelectedItem().toString();
                String credit = courseCredit.getText();
                String query = "DELETE FROM course WHERE c_id = ?";
                if(courseId.getText().isEmpty()||courseName.getText().isEmpty()||courseCredit.getText().isEmpty()||type.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null,"All fields need to filled");
                    return;
                }
                try{
                    con = Database.getConnection();
                    pstmt = con.prepareStatement(query);
                    pstmt.setString(1,Id);
                    int i = pstmt.executeUpdate();
                    if(i>0){
                        JOptionPane.showMessageDialog(null,"Course Deleted Successfully");
                        courseId.setText("");
                        courseName.setText("");
                        courseCredit.setText("");
                        tableLoad();
                    }else{
                        JOptionPane.showMessageDialog(null,"Error in deletion");
                        return;

                    }

                }catch (SQLException a){
                    JOptionPane.showMessageDialog(null,a.getMessage());
                }


            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminPanel adminPanel = new AdminPanel();
                dispose();
                adminPanel.setVisible(true);
            }
        });
    }
    public void createTable(){
        table1.setModel(new javax.swing.table.DefaultTableModel(null,new String[]{"CourseId","CourseName","CourseCredit","CourseType"}));
    }

    public void tableLoad(){
        int count;
        try{
            con = Database.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM course");
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            count = rsmd.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel)table1.getModel();
            dtm.setRowCount(0);
            while(rs.next()){
                Vector v = new Vector();
                for(int i = 1;i<=count;i++){
                    v.add(rs.getString(1));
                    v.add(rs.getString(2));
                    v.add((rs.getString(3)));
                    v.add(rs.getInt(4));
                }
                dtm.addRow(v);
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
}
