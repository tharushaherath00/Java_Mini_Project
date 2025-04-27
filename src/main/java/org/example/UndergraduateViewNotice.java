package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class UndergraduateViewNotice extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public UndergraduateViewNotice() {
        setTitle("Latest Notices");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadNoticeData();

        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(250, 255, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JLabel titleLabel = new JLabel("Latest Notices");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(55, 80, 55));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Title", "Content", "Posted Date", "Target Role"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setGridColor(new Color(200, 230, 200));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(55, 80, 55));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(55, 80, 55)));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void loadNoticeData() {
        String sql = "SELECT title, content, posted_date, target_role FROM Notices ORDER BY posted_date DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String title = rs.getString("title");
                String content = rs.getString("content");
                Timestamp postedDate = rs.getTimestamp("posted_date");
                String targetRole = rs.getString("target_role");

                tableModel.addRow(new Object[]{
                        title,
                        content,
                        postedDate.toString(),
                        targetRole
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading notices:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
