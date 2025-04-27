package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class attendance extends JFrame{
    private JTable table1;
    private JButton more80;
    private JButton button2;
    private JButton exactly80;
    private JButton less80withotMed;
    private JButton more80withMed;
    private JButton less80withMed;
    private JPanel mainAttendance;
    private JButton mainMenue;
    DefaultTableModel dtm;
    Connection conn;
    PreparedStatement pstmt;
    Statement stmt;
    ResultSet rs;

    public attendance(){
        setTitle("TO-Profile");
        setSize(600,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainAttendance);
        setVisible(true);

        String[] columns = {"Undergraduate_ID","Course_ID","Precentage"};
        createTable(table1,columns);

        more80.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = Database.getConnection();
                    stmt = conn.createStatement();
                    String query = "SELECT " +
                            "Student_ID, " +
                            "Course_ID, " +
                            "(SUM(CASE WHEN Status IN ('Present', 'Medical') THEN 1 ELSE 0 END) / COUNT(*)) * 100 AS `Attendance Percentage` " +
                            "FROM Atendance " +
                            "GROUP BY Student_ID, Course_ID " +
                            "HAVING (SUM(CASE WHEN Status IN ('Present', 'Medical') THEN 1 ELSE 0 END) / COUNT(*)) * 100 >= 80 " +
                            "ORDER BY Student_ID;";
                    rs = stmt.executeQuery(query);

                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);  // Clear existing rows

                    while (rs.next()) {
                        Vector<String> v = new Vector<>();
                        v.add(rs.getString("Student_ID"));
                        v.add(rs.getString("Course_ID"));
                        v.add(rs.getString("Attendance Percentage"));
                        model.addRow(v);
                    }



                } catch (SQLException ex) {
                    System.out.println("SQL Error: " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });


        exactly80.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = Database.getConnection();
                    stmt = conn.createStatement();
                    String query = "SELECT \n" +
                            "    Student_ID, \n" +
                            "    Course_ID,\n" +
                            "    (SUM(CASE WHEN Status IN ('Present', 'Medical') THEN 1 ELSE 0 END) / COUNT(*)) * 100 AS 'Attendance Percentage'\n" +
                            "FROM Atendance\n" +
                            "GROUP BY Student_ID, Course_ID\n" +
                            "HAVING (SUM(CASE WHEN Status IN ('Present', 'Medical') THEN 1 ELSE 0 END) / COUNT(*)) * 100 = 80\n" +
                            "ORDER BY Student_ID;\n";
                    rs = stmt.executeQuery(query);

                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);  // Clear existing rows

                    while (rs.next()) {
                        Vector<String> v = new Vector<>();
                        v.add(rs.getString("Student_ID"));
                        v.add(rs.getString("Course_ID"));
                        v.add(rs.getString("Attendance Percentage"));
                        model.addRow(v);
                    }



                } catch (SQLException ex) {
                    System.out.println("SQL Error: " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });
        less80withotMed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = Database.getConnection();
                    stmt = conn.createStatement();
                    String query = "SELECT \n" +
                            "    a.Student_ID,\n" +
                            "    a.Course_ID,\n" +
                            "    (SUM(CASE WHEN a.Status = 'Present' THEN 1 ELSE 0 END) / COUNT(*)) * 100 AS 'Attendance Percentage'\n" +
                            "FROM Atendance a\n" +
                            "WHERE NOT EXISTS (\n" +
                            "    SELECT 1\n" +
                            "    FROM Medical m\n" +
                            "    WHERE m.Student_ID = a.Student_ID\n" +
                            "      AND m.Course_ID = a.Course_ID\n" +
                            "      AND m.Status = 'Approved'\n" +
                            ")\n" +
                            "GROUP BY a.Student_ID, a.Course_ID\n" +
                            "HAVING ROUND((SUM(CASE WHEN a.Status = 'Present' THEN 1 ELSE 0 END) / COUNT(*)) * 100, 0) < 80\n" +
                            "ORDER BY a.Student_ID;\n";
                    rs = stmt.executeQuery(query);

                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);  // Clear existing rows

                    while (rs.next()) {
                        Vector<String> v = new Vector<>();
                        v.add(rs.getString("Student_ID"));
                        v.add(rs.getString("Course_ID"));
                        v.add(rs.getString("Attendance Percentage"));
                        model.addRow(v);
                    }



                } catch (SQLException ex) {
                    System.out.println("SQL Error: " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });
        more80withMed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = Database.getConnection();
                    stmt = conn.createStatement();
                    String query = "SELECT \n" +
                            "    a.Student_ID,\n" +
                            "    a.Course_ID,\n" +
                            "    ((SUM(CASE WHEN a.Status = 'Present' THEN 1 ELSE 0 END) + \n" +
                            "      (SELECT COUNT(*) \n" +
                            "       FROM Medical m\n" +
                            "       WHERE m.Student_ID = a.Student_ID\n" +
                            "         AND m.Course_ID = a.Course_ID\n" +
                            "         AND m.Status = 'Approved')) \n" +
                            "     / COUNT(*)) * 100 AS 'Attendance Percentage'\n" +
                            "FROM Atendance a\n" +
                            "GROUP BY a.Student_ID, a.Course_ID\n" +
                            "HAVING ROUND(((SUM(CASE WHEN a.Status = 'Present' THEN 1 ELSE 0 END) + \n" +
                            "                (SELECT COUNT(*) \n" +
                            "                 FROM Medical m\n" +
                            "                 WHERE m.Student_ID = a.Student_ID\n" +
                            "                   AND m.Course_ID = a.Course_ID\n" +
                            "                   AND m.Status = 'Approved')) \n" +
                            "               / COUNT(*)) * 100, 0) > 80\n" +
                            "ORDER BY a.Student_ID;\n";
                    rs = stmt.executeQuery(query);

                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);  // Clear existing rows

                    while (rs.next()) {
                        Vector<String> v = new Vector<>();
                        v.add(rs.getString("Student_ID"));
                        v.add(rs.getString("Course_ID"));
                        v.add(rs.getString("Attendance Percentage"));
                        model.addRow(v);
                    }



                } catch (SQLException ex) {
                    System.out.println("SQL Error: " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });
        less80withMed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = Database.getConnection();
                    stmt = conn.createStatement();
                    String query = "SELECT \n" +
                            "    a.Student_ID,\n" +
                            "    a.Course_ID,\n" +
                            "    ((SUM(CASE WHEN a.Status = 'Present' THEN 1 ELSE 0 END) + \n" +
                            "      (SELECT COUNT(*) \n" +
                            "       FROM Medical m\n" +
                            "       WHERE m.Student_ID = a.Student_ID\n" +
                            "         AND m.Course_ID = a.Course_ID\n" +
                            "         AND m.Status = 'Approved')) \n" +
                            "     / COUNT(*)) * 100 AS 'Attendance Percentage'\n" +
                            "FROM Atendance a\n" +
                            "GROUP BY a.Student_ID, a.Course_ID\n" +
                            "HAVING ROUND(((SUM(CASE WHEN a.Status = 'Present' THEN 1 ELSE 0 END) + \n" +
                            "                (SELECT COUNT(*) \n" +
                            "                 FROM Medical m\n" +
                            "                 WHERE m.Student_ID = a.Student_ID\n" +
                            "                   AND m.Course_ID = a.Course_ID\n" +
                            "                   AND m.Status = 'Approved')) \n" +
                            "               / COUNT(*)) * 100, 0) < 80\n" +
                            "ORDER BY a.Student_ID;\n";
                    rs = stmt.executeQuery(query);

                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setRowCount(0);  // Clear existing rows

                    while (rs.next()) {
                        Vector<String> v = new Vector<>();
                        v.add(rs.getString("Student_ID"));
                        v.add(rs.getString("Course_ID"));
                        v.add(rs.getString("Attendance Percentage"));
                        model.addRow(v);
                    }



                } catch (SQLException ex) {
                    System.out.println("SQL Error: " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });
    }
    public void createTable(JTable table,String[] columns){
        table.setModel(new DefaultTableModel(null, columns));
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(30, 144, 255)); // DodgerBlue
        header.setForeground(Color.WHITE);
    }

    public static void main(String[] args) {
        new attendance();
    }
}
