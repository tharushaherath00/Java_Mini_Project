package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(250, 255, 250));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(180, 70, 70));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Grade", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(55, 80, 55));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);


        tableModel = new DefaultTableModel(new String[]{"Course Code", "Course Name", "Grades", "SGPA", "CGPA"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setGridColor(new Color(200, 230, 200));
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(200, 230, 200));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(55, 80, 55));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(55, 80, 55)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UndergraduateDashboard(userId);
                dispose();
            }
        });

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

            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String courseId = rs.getString("Course_ID");
                String courseName = rs.getString("Course_Name");
                String grade = rs.getString("Grade");
                double sgpa = rs.getDouble("SGPA");
                double cgpa = rs.getDouble("CGPA");

                tableModel.addRow(new Object[]{courseId, courseName, grade, sgpa, cgpa});
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data!");
        }
    }
}
