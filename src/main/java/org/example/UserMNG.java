package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserMNG extends JFrame {
    private JTable userTable;
    private JTextField nicField, nameField, emailField, dobField, adminIdField;
    private JPasswordField passwordField;
    private JComboBox<String> departmentIdCombo;
    private JComboBox<Role> roleCombo;
    private JButton addButton, updateButton, deleteButton, clearButton, backButton;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private User adminUser;

    public UserMNG(User adminUser) {
        this.adminUser = adminUser;
        setTitle("User Management - Admin");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadUsers();
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Table for displaying users
        String[] columns = {"NIC", "Name", "Email", "DOB", "Department", "Role", "Admin ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFields();
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("User Details"));

        inputPanel.add(new JLabel("NIC:"));
        nicField = new JTextField();
        inputPanel.add(nicField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("DOB (YYYY-MM-DD):"));
        dobField = new JTextField();
        inputPanel.add(dobField);

        inputPanel.add(new JLabel("Department:"));
        departmentIdCombo = new JComboBox<>(new String[]{"ICT", "BT", "ET", "MDS"});
        inputPanel.add(departmentIdCombo);

        inputPanel.add(new JLabel("Role:"));
        roleCombo = new JComboBox<>(Role.values());
        roleCombo.addActionListener(e -> toggleAdminIdField());
        inputPanel.add(roleCombo);

        inputPanel.add(new JLabel("Admin ID (if applicable):"));
        adminIdField = new JTextField();
        adminIdField.setEnabled(false);
        inputPanel.add(adminIdField);

        mainPanel.add(inputPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        addButton = new JButton("Add User");
        addButton.addActionListener(e -> addUser());
        buttonPanel.add(addButton);

        updateButton = new JButton("Update User");
        updateButton.addActionListener(e -> updateUser());
        buttonPanel.add(updateButton);

        deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(e -> deleteUser());
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
    }

    private void toggleAdminIdField() {
        Role role = (Role) roleCombo.getSelectedItem();
        adminIdField.setEnabled(role == Role.ADMIN);
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = UserDAO.getAllUsers();
        for (User user : users) {
            String adminId = UserDAO.getAdminId(user.getNIC());
            tableModel.addRow(new Object[]{
                    user.getNIC(),
                    user.getName(),
                    user.getEmail(),
                    user.getDOB(),
                    user.getDepartmentId(),
                    user.getRole().getValue(),
                    adminId != null ? adminId : ""
            });
        }
    }

    private void populateFields() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            nicField.setText((String) tableModel.getValueAt(selectedRow, 0));
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            emailField.setText((String) tableModel.getValueAt(selectedRow, 2));
            dobField.setText((String) tableModel.getValueAt(selectedRow, 3));
            departmentIdCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
            roleCombo.setSelectedItem(Role.fromString((String) tableModel.getValueAt(selectedRow, 5)));
            adminIdField.setText((String) tableModel.getValueAt(selectedRow, 6));
            passwordField.setText("");
        }
    }

    private void addUser() {
        try {
            User user = createUserFromFields();
            if (user == null) return;

            UserDAO.addUser(user, adminIdField.getText().trim());
            loadUsers();
            clearFields();
            JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = createUserFromFields();
            if (user == null) return;

            UserDAO.updateUser(user, adminIdField.getText().trim());
            loadUsers();
            clearFields();
            JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String nic = (String) tableModel.getValueAt(selectedRow, 0);
                UserDAO.deleteUser(nic);
                loadUsers();
                clearFields();
                JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        nicField.setText("");
        nameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        dobField.setText("");
        departmentIdCombo.setSelectedIndex(0);
        roleCombo.setSelectedIndex(0);
        adminIdField.setText("");
        userTable.clearSelection();
    }

    private User createUserFromFields() {
        String nic = nicField.getText().trim();
        String name = nameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();
        String dobStr = dobField.getText().trim();
        String departmentId = (String) departmentIdCombo.getSelectedItem();
        Role role = (Role) roleCombo.getSelectedItem();

        if (nic.isEmpty() || name.isEmpty() || email.isEmpty() || dobStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields except password are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        java.sql.Date dob;
        try {
            dob = new java.sql.Date(sdf.parse(dobStr).getTime());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid DOB format (use YYYY-MM-DD)!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String department;
        switch (departmentId) {
            case "ICT": department = "Information and Communication Technology"; break;
            case "BT": department = "Biosystems Technology"; break;
            case "ET": department = "Engineering Technology"; break;
            case "MDS": department = "Multidisciplinary Studies"; break;
            default: department = "";
        }

        return new User(nic, name, password, email, dob, departmentId, department, role);
    }

}