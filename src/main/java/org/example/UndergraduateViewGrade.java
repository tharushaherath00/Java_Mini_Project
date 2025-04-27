package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UndergraduateViewGrade extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private String userId;

    public UndergraduateViewGrade(String userId) {
        this.userId = userId;
        setTitle("Grade");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);


    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 255, 250));
        setContentPane(mainPanel);


        JLabel titleLabel = new JLabel("Grade");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(55, 80, 55)); // Dark green text
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        tableModel = new DefaultTableModel(new String[]{"Course Code", "Course Name", "Grades", "SGPA", "CGPA"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setGridColor(new Color(200, 230, 200)); // Light green grid
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(200, 230, 200)); // Light green selection

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(55, 80, 55)); // Dark green header background
        header.setForeground(Color.WHITE); // White header text

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(55, 80, 55))); // Dark green border
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadData();


    }

    private void loadData() {
        String sql = "SELECT spv.Course_ID, spv.Course_Name, spv.Grade, sgpa.SGPA, cgpa.CGPA " +
                "FROM StudentPerformanceViewn spv " +
                "LEFT JOIN SGPA_View sgpa ON spv.Student_ID = sgpa.StuId " +
                "LEFT JOIN CGPA_View cgpa ON spv.Student_ID = cgpa.StuId " +
                "WHERE spv.Student_ID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId); // use the userId passed to the constructor

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String courseId = rs.getString("Course_ID");
                String courseName = rs.getString("Course_Name");
                String grade = rs.getString("Grade");
                double sgpa = rs.getDouble("SGPA");
                double cgpa = rs.getDouble("CGPA");

                tableModel.addRow(new Object[]{courseId, courseName, grade, sgpa, cgpa});
            }

            rs.close(); // Optional because try-with-resources will close it automatically, but it's okay to keep.

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data!");
        }
    }




}
