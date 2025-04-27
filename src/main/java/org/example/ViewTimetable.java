package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ViewTimetable {
    private JTable table1;
    private JButton mainMenuButton;
    private JPanel ViewTimetable;
    private JPanel ViewTimetablePanel;
    private Connection myconn;
    private User currentUser;

    // Constructor
    public ViewTimetable(User user) {
        this.currentUser = user;

        // Initialize components
        table1 = new JTable();
        mainMenuButton = new JButton("Main Menu");
        ViewTimetablePanel = new JPanel();

        // Set up layout and add components
        ViewTimetablePanel.setLayout(new BoxLayout(ViewTimetablePanel, BoxLayout.Y_AXIS));
        ViewTimetablePanel.add(new JScrollPane(table1)); // Make table scrollable
        ViewTimetablePanel.add(mainMenuButton);

        // Connect to the database and load data
        try {
            myconn = Database.getConnection();
            loadTimetableData(); // Load data from the database into the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection error.");
        }

        // Action listener for the main menu button
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to profileView when main menu button is clicked
                JFrame frame = new JFrame("Technical Officer Profile");
                profileView profile = new profileView(currentUser);  // Ensure profileView is correctly imported and instantiated
                frame.setContentPane(profile.getMainPanel());  // Ensure getMainPanel() is defined in profileView
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

                // Close the current ViewTimetable window
                SwingUtilities.getWindowAncestor(ViewTimetablePanel).dispose();
            }
        });

        // Create and display the frame
        JFrame timetableFrame = new JFrame("View Timetable");
        timetableFrame.setContentPane(ViewTimetablePanel);
        timetableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        timetableFrame.pack();
        timetableFrame.setVisible(true);
    }

    // Method to load timetable data from the database and display it in JTable
    private void loadTimetableData() {
        String query = "SELECT * FROM Timetable"; // Adjust query based on your table structure
        try (PreparedStatement stmt = myconn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Get the metadata and column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0); // Clear previous rows

            // Loop through the ResultSet and add rows to the table
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i)); // Add each column value from ResultSet
                }
                model.addRow(row); // Add the row to the table model
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading timetable data.");
        }
    }

    // Getter for the main panel
    public JPanel getViewTimetablePanel() {
        return ViewTimetablePanel;
    }

    // Main method to run the ViewTimetable form
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User("tofficer@example.com", Role.TECHNICAL_OFFICER); // Pass a test user
            new ViewTimetable(testUser);
        });
    }
}
