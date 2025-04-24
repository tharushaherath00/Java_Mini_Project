package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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
    private JLabel lectureId;
//    private JTextField lecturer;
    private JComboBox department;
    private JLabel depId;
    private JComboBox lectureSelect;

    Connection con;
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;
    Lecturer selectedLecturer;
    String lec;



    public courseMPUI() {
        setTitle("Course Management");
        setSize(1000,500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(main);
        setVisible(true);
        type.addItem("Practical");
        type.addItem("Theory");
        type.addItem("Theory + Practical");
        department.addItem("ET");
        department.addItem("ICT");
        department.addItem("BST");
        createTable();
        tableLoad();
        loadLecturers();


        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int Ccredit;
                String credit = courseCredit.getText();
//                String Id = courseId.getText();
//                String name = courseName.getText();
//                String Ctype = type.getSelectedItem().toString();
//
////                for radio button
//
////                if(practical.isSelected()){
////                    type = "Practical";
////                }else
////                    type = "Theory";
//
//                String dep = department.getSelectedItem().toString();
//                selectedLecturer = (Lecturer) lectureSelect.getSelectedItem();
//                lec = selectedLecturer.getId();

                String query = "INSERT INTO course(Course_ID,Course_Name,Course_Type,Credit,Dep_ID,Lecturer_ID) VALUES(?,?,?,?,?,?)";
                String checkUser = "SELECT * FROM course WHERE Course_ID = ?";
                if(courseId.getText().isEmpty()||courseName.getText().isEmpty()||courseCredit.getText().isEmpty()||type.getSelectedItem()==null||department.getSelectedItem()==null||lectureSelect.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null,"Please fill in all the fields.");
                    return;
                }
                try{
                    con = Database.getConnection();
                    pstmt =con.prepareStatement(checkUser);
                    pstmt.setString(1,courseId.getText());
                    rs = pstmt.executeQuery();
                    selectedLecturer = (Lecturer) lectureSelect.getSelectedItem();
                    if(rs.next()){
                        JOptionPane.showMessageDialog(null,"Course already exist");
                        return;
                    }else{
//                        try {
//                            Ccredit = Integer.parseInt(credit);
//                        } catch (NumberFormatException nfe) {
//                            JOptionPane.showMessageDialog(null, "Course credit must be a number.");
//                            return;
//                        }

                        Ccredit = validateCredit(credit);
                        pstmt = con.prepareStatement(query);
                        pstmt.setString(1,courseId.getText());
                        pstmt.setString(2,courseName.getText());
                        pstmt.setString(3,type.getSelectedItem().toString());
                        pstmt.setInt(4,Ccredit);
                        pstmt.setString(5,department.getSelectedItem().toString());
                        pstmt.setString(6,selectedLecturer.getId());
                        int i = pstmt.executeUpdate();
                        if(i>0) {
                            JOptionPane.showMessageDialog(null, "Course added successfully");
                            courseId.setText("");
                            courseName.setText("");
                            courseCredit.setText("");
                            lectureSelect.setSelectedItem(null);
                            department.setSelectedItem(null);
                            type.setSelectedItem(null);
                            tableLoad();
                        }
                    }
                }catch(ValidationException u){
                    JOptionPane.showMessageDialog(null,u.getMessage());
                }
                catch(SQLException a){
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
                type.setSelectedItem(dtm.getValueAt(selectRow,3));
                courseCredit.setText(dtm.getValueAt(selectRow,2).toString());
//                lectureSelect.setSelectedItem(dtm.getValueAt(selectRow,4));
                department.setSelectedItem(dtm.getValueAt(selectRow,5));





            }
        });
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String Id = courseId.getText();
//                String name = courseName.getText();
//                String Ctype = type.getSelectedItem().toString();
                int Ccredit;
                String credit = courseCredit.getText();
//                for radio button

//                if(practical.isSelected()){
//                    type = "Practical";
//                }else
//                    type = "Theory";

//                String dep = department.getSelectedItem().toString();
//                selectedLecturer = (Lecturer) lectureSelect.getSelectedItem();
//                lec = selectedLecturer.getId();

                String query = "UPDATE course SET Course_Name = ?,Course_Type = ?,Credit = ?,Lecturer_ID = ?,Dep_ID = ? WHERE Course_ID = ?";
                //String checkUser = "SELECT * FROM course WHERE c_id = ?";
                if(courseId.getText().isEmpty()||courseName.getText().isEmpty()||courseCredit.getText().isEmpty()||type.getSelectedItem()==null||department.getSelectedItem()==null||lectureSelect.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null,"Please fill in all the fields.");
                    return;
                }
                try{
                    con = Database.getConnection();
                    selectedLecturer = (Lecturer) lectureSelect.getSelectedItem();
                    //pstmt =con.prepareStatement(checkUser);
                    //pstmt.setString(1,Id);
                    //rs = pstmt.executeQuery();
//                    if(rs.next()){
//                        JOptionPane.showMessageDialog(null,"Course already exist");
//                        return;
//                    }
//                    try {
//                        Ccredit = Integer.parseInt(credit);
//                    } catch (NumberFormatException nfe) {
//                        JOptionPane.showMessageDialog(null, "Course credit must be a number.");
//                        return;
//                    }

                        Ccredit = validateCredit(credit);
                        pstmt = con.prepareStatement(query);
                        pstmt.setString(6,courseId.getText());
                        pstmt.setString(1,courseName.getText());
                        pstmt.setString(2,type.getSelectedItem().toString());
                        pstmt.setInt(3,Ccredit);
                        pstmt.setString(4,selectedLecturer.getId());
                        pstmt.setString(5,department.getSelectedItem().toString());

                        int i = pstmt.executeUpdate();
                        if(i>0) {
                            JOptionPane.showMessageDialog(null, "Course Updated successfully");
                            courseId.setText("");
                            courseName.setText("");
                            courseCredit.setText("");
                            type.setSelectedItem(null);
                            department.setSelectedItem(null);
                            lectureSelect.setSelectedItem(null);
                            tableLoad();
                        }

                }catch(ValidationException u){
                    JOptionPane.showMessageDialog(null,u.getMessage());
                }
                catch(SQLException a){
                    JOptionPane.showMessageDialog(null,a.getMessage());
                }catch(Exception q){
                    JOptionPane.showMessageDialog(null,q.getMessage());
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String Id = courseId.getText();
//                String name = courseName.getText();
//                String Ctype = type.getSelectedItem().toString();
//                String credit = courseCredit.getText();
                String query = "DELETE FROM course WHERE Course_ID = ?";
                if(courseId.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"Please provide Course Id to delete");
                    return;
                }
                try{
                    con = Database.getConnection();
                    pstmt = con.prepareStatement(query);
                    pstmt.setString(1,courseId.getText());
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
//        lectureSelect.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                 selectedLecturer = (Lecturer) lectureSelect.getSelectedItem();
//                if (selectedLecturer == null) {
//                    JOptionPane.showMessageDialog(null, "Please select a lecturer.");
//                    return;
//                }
//                lec = selectedLecturer.getId();
//
//            }
//        });
    }
    public void createTable(){
        table1.setModel(new javax.swing.table.DefaultTableModel(null,new String[]{"CourseId","CourseName","CourseCredit","CourseType","Lecturer_ID","Dep_ID"}));
        table1.setRowHeight(25); // Make rows taller
        table1.setShowGrid(true);
        table1.setGridColor(Color.LIGHT_GRAY);
        table1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        JTableHeader header = table1.getTableHeader();
        header.setBackground(new Color(30, 144, 255)); // DodgerBlue
        header.setForeground(Color.WHITE);
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
//            while(rs.next()){
//                Vector v = new Vector();
//                for(int i = 1;i<=count;i++){
//                    v.add(rs.getString(1));
//                    v.add(rs.getString(2));
//                    v.add((rs.getString(3)));
//                    v.add(rs.getInt(4));
//                }
//                dtm.addRow(v);
//            }
            while(rs.next()){
                Vector<String> v = new Vector<>();
                v.add(rs.getString("Course_ID"));
                v.add(rs.getString("Course_Name"));
                v.add(String.valueOf(rs.getInt("Credit"))); // convert to String for consistency
                v.add(rs.getString("Course_Type"));
                v.add(rs.getString("Lecturer_ID"));
                v.add(rs.getString("Dep_ID"));
                dtm.addRow(v);
            }

        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    public static void main(String[] args) {
        new courseMPUI();
    }

    public void loadLecturers() {
        try {
            con = Database.getConnection();
            pstmt = con.prepareStatement(
                    "SELECT l.Lecturer_ID, u.Name FROM lecturer l JOIN user u ON l.NIC = u.NIC"
            );
            rs = pstmt.executeQuery();
            lectureSelect.removeAllItems();
            while (rs.next()) {
                String id = rs.getString("Lecturer_ID");
                String name = rs.getString("Name");
                lectureSelect.addItem(new Lecturer(id, name));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    public class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }


    private int validateCredit(String creditStr) throws ValidationException {
        try {
            int credit = Integer.parseInt(creditStr);
            if (credit <= 0 || credit >= 5) {
                throw new ValidationException("Course credit must be greater than 0 and less than 5.");
            }
            return credit;
        } catch (NumberFormatException e) {
            throw new ValidationException("Course credit must be a number.");
        }
    }


}
