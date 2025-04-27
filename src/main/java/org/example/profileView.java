package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class profileView {
    private JPanel main;  // Main panel for the profile view form
    private JPanel profile_main;
    private JLabel welcomeTechnicalOfficerProfileLabel;
    private JTextField nameTextField;
    private JTextField email;
    private JTextField dobto;
    private JTextField departmentid;
    private JTextField department;
    private JTextField toid;
    private JTextField depid;
    private JTextField nic;
    private JButton technicalOfficerProfileUpdateButton;
    private JButton maintainMedicalButton;
    private JButton ViewNoticebutton;
    private JButton viewAttendanceButton;
    private JButton addAttendanceButton;
    private JPanel full;
    private JButton viewTimeTableButton;
    private JPanel Buttonon;
    private JButton attendanceByPercentageButton;
    private JTextField studentid;
    private JTextField coursecode;
    private JTextField sessiontype;
    private JTextField status;
    private JTextField date;

    private Connection myconn;
    private User currentUser;

    // Constructor
    public profileView(User user) {
        this.currentUser = user;

        try {
            myconn = Database.getConnection();
            loadProfileData(user.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection error.");
        }

        // Action listener for Add Attendance Button
        addAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the Add Attendance form when the button is clicked
                JFrame addAttendanceFrame = new JFrame("Add Attendance");
                addAttendanceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addAttendanceFrame.setContentPane(new AddAttendance(user).getMainPanel());
                addAttendanceFrame.pack();
                addAttendanceFrame.setVisible(true);
            }
        });

        // Action listener for View Attendance Button
        viewAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When the viewAttendanceButton is clicked, open the ViewAttendance form
                new ViewAttendance(currentUser); // Pass currentUser to ViewAttendance
            }
        });

        // Action listener for View Timetable Button
        viewTimeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new JFrame to display the timetable
                JFrame timetableFrame = new JFrame("View Timetable");
                JPanel timetablePanel = new JPanel(); // Panel to hold the table and other components
                timetableFrame.setContentPane(timetablePanel);

                // Retrieve the timetable from the database
                try (Connection conn = Database.getConnection()) {
                    String query = "SELECT Course_ID, Day, Start_Time, End_Time, Room, Lecturer_ID FROM Timetable"; // Adjust query based on your table structure
                    PreparedStatement stmt = conn.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    // Create a table model to display the timetable
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("Course ID");
                    model.addColumn("Day");
                    model.addColumn("Start Time");
                    model.addColumn("End Time");
                    model.addColumn("Room");
                    model.addColumn("Lecturer ID");

                    // Add rows to the table model
                    while (rs.next()) {
                        Object[] row = new Object[6];
                        row[0] = rs.getString("Course_ID");
                        row[1] = rs.getString("Day");
                        row[2] = rs.getTime("Start_Time");
                        row[3] = rs.getTime("End_Time");
                        row[4] = rs.getString("Room");
                        row[5] = rs.getString("Lecturer_ID");
                        model.addRow(row);
                    }

                    // Create a JTable to display the timetable
                    JTable timetableTable = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(timetableTable); // Make the table scrollable
                    timetablePanel.add(scrollPane);

                    // Set up the frame
                    timetableFrame.setSize(800, 400); // Adjust size as needed
                    timetableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    timetableFrame.setVisible(true);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error loading timetable: " + ex.getMessage());
                }
            }
        });

        // Show the profileView form
        JFrame frame = new JFrame("Technical Officer Profile");
        frame.setContentPane(main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        maintainMedicalButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewMedical();

            }
        });
        technicalOfficerProfileUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    new UpdateTOProfile(user);

            }
        });
        ViewNoticebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewNotice();
            }
        });
        attendanceByPercentageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new attendance();
            }
        });
    }

    // Method to load the profile data from the database
    private void loadProfileData(String emailParam) {
        String query = "SELECT u.Name, u.NIC, u.Email, u.DOB, u.Department_ID, u.Department, t.TO_ID, t.Dep_ID " +
                "FROM User u JOIN Technical_Officer t ON u.NIC = t.NIC WHERE u.Email = ?";

        try (PreparedStatement stmt = myconn.prepareStatement(query)) {
            stmt.setString(1, emailParam);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nameTextField.setText(rs.getString("Name"));
                    nic.setText(rs.getString("NIC"));
                    email.setText(rs.getString("Email"));
                    dobto.setText(rs.getString("DOB"));
                    departmentid.setText(rs.getString("Department_ID"));
                    department.setText(rs.getString("Department"));
                    toid.setText(rs.getString("TO_ID"));
                    depid.setText(rs.getString("Dep_ID"));

                    // Optionally, update currentUser (if needed)
                    currentUser.setName(rs.getString("Name"));
                    currentUser.setEmail(rs.getString("Email"));
                    currentUser.setDOB(Date.valueOf(rs.getString("DOB")));
                    currentUser.setDepartmentId(rs.getString("Department_ID"));
                    currentUser.setDepartment(rs.getString("Department"));
                    currentUser.setRole(Role.TECHNICAL_OFFICER); // If role needs to be updated dynamically
                } else {
                    JOptionPane.showMessageDialog(null, "No profile found for this user.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading profile: " + ex.getMessage());
        }
    }

    // Getter method for main panel
    public JPanel getMainPanel() {
        return main;  // This allows other classes to access the main panel of the profile view
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Pass a test user for demonstration
            User testUser = new User("tofficer@example.com", Role.TECHNICAL_OFFICER);
            new profileView(testUser);
        });
    }
}
