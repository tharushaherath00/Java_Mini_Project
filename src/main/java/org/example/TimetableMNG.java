package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TimetableMNG extends JFrame {
    private JTable timetableTable;
    private JTextField timetableIdField, roomField;
    private JComboBox<String> courseIdCombo, dayCombo, departmentCombo;
    private JComboBox<String> lecturerIdCombo;
    private JTextField startTimeField, endTimeField;
    private JButton addButton, updateButton, deleteButton, clearButton, backButton;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private User adminUser;

    public TimetableMNG(User adminUser) {
        this.adminUser = adminUser;
        setTitle("Timetable Management - Admin");
        setSize(900, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadTimetables("ICT");
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Department:"));
        departmentCombo = new JComboBox<>(new String[]{"ICT", "ET", "BST"});
        departmentCombo.addActionListener(e -> {
            String dept = (String) departmentCombo.getSelectedItem();
            loadTimetables(dept);
            updateCourseAndLecturerCombos(dept);
        });
        filterPanel.add(departmentCombo);
        mainPanel.add(filterPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Course ID", "Day", "Start Time", "End Time", "Room", "Lecturer ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        timetableTable = new JTable(tableModel);
        timetableTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFields();
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(timetableTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Timetable Details"));

        inputPanel.add(new JLabel("Timetable ID:"));
        timetableIdField = new JTextField();
        timetableIdField.setEditable(false);
        inputPanel.add(timetableIdField);

        inputPanel.add(new JLabel("Course ID:"));
        courseIdCombo = new JComboBox<>();
        inputPanel.add(courseIdCombo);

        inputPanel.add(new JLabel("Day:"));
        dayCombo = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
        inputPanel.add(dayCombo);

        inputPanel.add(new JLabel("Start Time (HH:MM):"));
        startTimeField = new JTextField();
        inputPanel.add(startTimeField);

        inputPanel.add(new JLabel("End Time (HH:MM):"));
        endTimeField = new JTextField();
        inputPanel.add(endTimeField);

        inputPanel.add(new JLabel("Room:"));
        roomField = new JTextField();
        inputPanel.add(roomField);

        inputPanel.add(new JLabel("Lecturer ID:"));
        lecturerIdCombo = new JComboBox<>();
        inputPanel.add(lecturerIdCombo);

        mainPanel.add(inputPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        addButton = new JButton("Add Timetable");
        addButton.addActionListener(e -> addTimetable());
        buttonPanel.add(addButton);

        updateButton = new JButton("Update Timetable");
        updateButton.addActionListener(e -> updateTimetable());
        buttonPanel.add(updateButton);

        deleteButton = new JButton("Delete Timetable");
        deleteButton.addActionListener(e -> deleteTimetable());
        buttonPanel.add(deleteButton);

        clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(clearButton);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new AdminPanel(adminUser);
            dispose();
        });
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        updateCourseAndLecturerCombos("ICT");
    }

    private void updateCourseAndLecturerCombos(String departmentId) {
        courseIdCombo.removeAllItems();
        for (String courseId : TimetableDAO.getAvailableCourses(departmentId)) {
            courseIdCombo.addItem(courseId);
        }

        lecturerIdCombo.removeAllItems();
        for (String lecturerId : TimetableDAO.getAvailableLecturers(departmentId)) {
            lecturerIdCombo.addItem(lecturerId);
        }
    }

    private void loadTimetables(String departmentId) {
        tableModel.setRowCount(0);
        List<Timetable> timetables = TimetableDAO.getAllTimetables(departmentId);
        for (Timetable timetable : timetables) {
            tableModel.addRow(new Object[]{
                    timetable.getTimetableId(),
                    timetable.getCourseId(),
                    timetable.getDay(),
                    timetable.getStartTime(),
                    timetable.getEndTime(),
                    timetable.getRoom(),
                    timetable.getLecturerId()
            });
        }
    }

    private void populateFields() {
        int selectedRow = timetableTable.getSelectedRow();
        if (selectedRow >= 0) {
            timetableIdField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 0)));
            courseIdCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
            dayCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
            startTimeField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            endTimeField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            roomField.setText((String) tableModel.getValueAt(selectedRow, 5));
            lecturerIdCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 6));
        }
    }

    private void addTimetable() {
        try {
            Timetable timetable = createTimetableFromFields(true);
            if (timetable == null) return;

            TimetableDAO.addTimetable(timetable);
            loadTimetables((String) departmentCombo.getSelectedItem());
            clearFields();
            JOptionPane.showMessageDialog(this, "Timetable added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding timetable: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTimetable() {
        int selectedRow = timetableTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a timetable to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Timetable timetable = createTimetableFromFields(false);
            if (timetable == null) return;

            TimetableDAO.updateTimetable(timetable);
            loadTimetables((String) departmentCombo.getSelectedItem());
            clearFields();
            JOptionPane.showMessageDialog(this, "Timetable updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating timetable: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTimetable() {
        int selectedRow = timetableTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a timetable to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this timetable?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int timetableId = (int) tableModel.getValueAt(selectedRow, 0);
                TimetableDAO.deleteTimetable(timetableId);
                loadTimetables((String) departmentCombo.getSelectedItem());
                clearFields();
                JOptionPane.showMessageDialog(this, "Timetable deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting timetable: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        timetableIdField.setText(String.valueOf(TimetableDAO.getNextTimetableId()));
        courseIdCombo.setSelectedIndex(0);
        dayCombo.setSelectedIndex(0);
        startTimeField.setText("");
        endTimeField.setText("");
        roomField.setText("");
        lecturerIdCombo.setSelectedIndex(0);
        timetableTable.clearSelection();
    }

    private Timetable createTimetableFromFields(boolean isNew) {
        String timetableIdStr = timetableIdField.getText().trim();
        String courseId = (String) courseIdCombo.getSelectedItem();
        String day = (String) dayCombo.getSelectedItem();
        String startTimeStr = startTimeField.getText().trim();
        String endTimeStr = endTimeField.getText().trim();
        String room = roomField.getText().trim();
        String lecturerId = (String) lecturerIdCombo.getSelectedItem();

        if (courseId == null || day == null || startTimeStr.isEmpty() || endTimeStr.isEmpty() || room.isEmpty() || lecturerId == null) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (!room.matches("^(LR|LAB)[0-9]{2,3}$")) {
            JOptionPane.showMessageDialog(this, "Invalid room format (e.g., LR101, LAB01)!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setLenient(false);
        Time startTime, endTime;
        try {
            startTime = new Time(timeFormat.parse(startTimeStr).getTime());
            endTime = new Time(timeFormat.parse(endTimeStr).getTime());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid time format (use HH:MM)!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (!startTime.before(endTime)) {
            JOptionPane.showMessageDialog(this, "Start time must be before end time!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int timetableId;
        if (isNew) {
            timetableId = TimetableDAO.getNextTimetableId();
        } else {
            try {
                timetableId = Integer.parseInt(timetableIdStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Timetable ID!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        return new Timetable(timetableId, courseId, day, startTime, endTime, room, lecturerId);
    }
}