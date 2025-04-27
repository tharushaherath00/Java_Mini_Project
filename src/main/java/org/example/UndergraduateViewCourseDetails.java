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

public class UndergraduateViewCourseDetails extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> courseComboBox;
    private JComboBox<String> materialComboBox;
    private JLabel downloadLinkLabel;
    private String userId;

    private Map<String, String> courseNameToIdMap = new HashMap<>();

    public UndergraduateViewCourseDetails(String userId) {
        this.userId = userId;
        setTitle("Enrolled Course Details");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadCourseData();
        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(250, 255, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
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

        JLabel titleLabel = new JLabel("Enrolled Courses");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(55, 80, 55));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Course ID");
        tableModel.addColumn("Course Name");

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(28);
        table.setGridColor(new Color(200, 230, 200));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(55, 80, 55));
        header.setForeground(Color.white);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(55, 80, 55)));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(250, 255, 250));

        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectPanel.setBackground(new Color(250, 255, 250));

        courseComboBox = new JComboBox<>();
        courseComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        courseComboBox.setPreferredSize(new Dimension(250, 30));

        materialComboBox = new JComboBox<>();
        materialComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        materialComboBox.setPreferredSize(new Dimension(200, 30));

        selectPanel.add(new JLabel("Select Course:"));
        selectPanel.add(courseComboBox);
        selectPanel.add(Box.createHorizontalStrut(20));
        selectPanel.add(new JLabel("Select Material:"));
        selectPanel.add(materialComboBox);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(new Color(250, 255, 250));

        JButton downloadButton = new JButton("Download Materials");
        downloadButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        downloadButton.setBackground(new Color(70, 130, 180));
        downloadButton.setForeground(Color.WHITE);
        downloadButton.setFocusPainted(false);
        downloadButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        downloadLinkLabel = new JLabel("Download link will appear here.");
        downloadLinkLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        downloadLinkLabel.setForeground(new Color(55, 80, 55));

        actionPanel.add(downloadButton);
        actionPanel.add(Box.createHorizontalStrut(20));
        actionPanel.add(downloadLinkLabel);

        bottomPanel.add(selectPanel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(actionPanel);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);


        backButton.addActionListener(e -> {
            new UndergraduateDashboard(userId);
            dispose();
        });

        courseComboBox.addActionListener(e -> loadMaterialsForSelectedCourse());

        downloadButton.addActionListener(e -> {
            String selectedCourseName = (String) courseComboBox.getSelectedItem();
            String selectedMaterial = (String) materialComboBox.getSelectedItem();

            if (selectedCourseName != null && selectedMaterial != null) {
                String downloadLink = downloadCourseMaterials(selectedCourseName, selectedMaterial);
                downloadLinkLabel.setText("<html><a href='" + downloadLink + "'>" + downloadLink + "</a></html>");
            } else {
                JOptionPane.showMessageDialog(UndergraduateViewCourseDetails.this,
                        "Please select both a course and a material type.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadCourseData() {
        String sql = "SELECT Course_ID, Course_Name FROM Course";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String courseId = rs.getString("Course_ID");
                String courseName = rs.getString("Course_Name");
                courseComboBox.addItem(courseName);
                courseNameToIdMap.put(courseName, courseId);
                tableModel.addRow(new Object[]{courseId, courseName});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMaterialsForSelectedCourse() {
        String selectedCourseName = (String) courseComboBox.getSelectedItem();
        materialComboBox.removeAllItems();

        if (selectedCourseName != null) {
            String selectedCourseId = courseNameToIdMap.get(selectedCourseName);

            String sql = "SELECT filename FROM files WHERE course_id = ?";

            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, selectedCourseId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String filename = rs.getString("filename");
                    materialComboBox.addItem(filename);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error loading materials: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String downloadCourseMaterials(String courseName, String materialType) {
        String coursePart = courseName.replace(" ", "_");
        String materialPart = materialType.replace(" ", "_");
        return "http://example.com/download/" + coursePart + "/" + materialPart;
    }
}
