package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UndergraduateViewTimeTable extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private String userId;

    public UndergraduateViewTimeTable(String userId) {
        this.userId = userId;
        setTitle("Timetable");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadTimeTableData();
        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 255, 245));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 255, 245));


        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(180, 70, 70));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        topPanel.add(backButton, BorderLayout.WEST);


        JLabel titleLabel = new JLabel(" Weekly Timetable");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(40, 70, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);


        tableModel = new DefaultTableModel();
        tableModel.addColumn("Course ID");
        tableModel.addColumn("Day");
        tableModel.addColumn("Start Time");
        tableModel.addColumn("End Time");
        tableModel.addColumn("Room");
        tableModel.addColumn("Lecturer ID");

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setGridColor(new Color(180, 220, 180));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(40, 70, 40));
        header.setForeground(Color.white);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(40, 70, 40)));

        mainPanel.add(scrollPane, BorderLayout.CENTER);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UndergraduateDashboard(userId);
            }
        });
    }

    private void loadTimeTableData() {
        String sql = "SELECT Course_ID, Day, Start_Time, End_Time, Room, Lecturer_ID FROM Timetable";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String courseId = rs.getString("Course_ID");
                String day = rs.getString("Day");
                String startTime = rs.getString("Start_Time");
                String endTime = rs.getString("End_Time");
                String room = rs.getString("Room");
                String lecturerId = rs.getString("Lecturer_ID");

                tableModel.addRow(new Object[]{courseId, day, startTime, endTime, room, lecturerId});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
