package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UndergraduateViewAttendance extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JRadioButton medicalRadio;
    private Object login;
    private String userId;

    public UndergraduateViewAttendance(String userId) {
        this.userId = userId;
        setTitle("Attendance Viewer");
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


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(250, 255, 250));
        mainPanel.add(topPanel, BorderLayout.NORTH);

        medicalRadio = new JRadioButton("Include Medical");
        medicalRadio.setBackground(new Color(250, 255, 250));
        medicalRadio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        medicalRadio.setForeground(new Color(55, 80, 55));
        topPanel.add(medicalRadio);

        JButton loadButton = new JButton("Load Attendance");
        loadButton.setBackground(new Color(120, 180, 120));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loadButton.setFocusPainted(false);
        topPanel.add(loadButton);


        tableModel = new DefaultTableModel(new String[]{"Student ID", "Course ID", "Session Type", "Percentage"}, 0);
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


        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAttendanceData(userId);
            }
        });
    }

    private void loadAttendanceData(String userId) {
        tableModel.setRowCount(0);
        boolean includeMedical = medicalRadio.isSelected();

        String sql = "SELECT a.Student_ID, a.Course_ID, a.Session_Type, a.Status " +
                "FROM Atendance a " +
                "JOIN Student s ON a.Student_ID = s.Student_ID " +
                "WHERE s.Student_ID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {

                Map<String, int[]> courseAttendance = new HashMap<>();

                while (rs.next()) {
                    String studentId = rs.getString(1);
                    String courseId = rs.getString("Course_ID");
                    String sessionType = rs.getString("Session_Type");
                    String status = rs.getString("Status");

                    String key = courseId + "-" + sessionType;
                    int[] counts = courseAttendance.getOrDefault(key, new int[3]);

                    counts[0]++;

                    if ("Present".equalsIgnoreCase(status)) {
                        counts[1]++;
                    } else if ("Medical".equalsIgnoreCase(status)) {
                        counts[2]++;
                    }

                    courseAttendance.put(key, counts);
                }


                for (Map.Entry<String, int[]> entry : courseAttendance.entrySet()) {
                    String[] parts = entry.getKey().split("-");
                    String courseId = parts[0];
                    String sessionType = parts[1];
                    int[] counts = entry.getValue();

                    int total = counts[0];
                    int present = counts[1];
                    int medical = counts[2];

                    double percentageNoMedical = total == 0 ? 0 : (present * 100.0) / total;
                    double percentageWithMedical = total == 0 ? 0 : ((present + medical) * 100.0) / total;

                    tableModel.addRow(new Object[]{
                            userId,
                            courseId,
                            sessionType,
                            includeMedical ? String.format("%.2f", percentageWithMedical) : String.format("%.2f", percentageNoMedical)
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading attendance:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
