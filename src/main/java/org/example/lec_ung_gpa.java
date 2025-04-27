package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;

public class lec_ung_gpa extends JFrame {
    private JTextField textField1;
    private JButton searchButton;
    private JTable table1;
    private JPanel Main_P;
    private JButton backbtn;
    private MyDBConnecter mdc;
    private Connection con;

    public lec_ung_gpa() {

        setTitle("Lecture Materials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        setContentPane(Main_P);
        setVisible(true);

        mdc = new MyDBConnecter();
        con = mdc.getMyConnection();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String index = textField1.getText().trim();

                if (index.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter the index");
                    return;
                }
                calculateGPA(index);

            }
        });

        backbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lec_Stu dash2 = new lec_Stu();
                dash2.setVisible(true);
                dispose();
            }
        });
    }

    public void calculateGPA(String index) {
        try {
            String sql = "SELECT Course_code, Assignment_01, Assignment_02, Quiz_01, Quiz_02, Quiz_03, Quiz_04, Mid_theory, Mid_practical, End_theory, End_practical FROM marks WHERE Stu_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, index);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            model.setColumnIdentifiers(new String[]{"Stu_id", "Course_ID", "Grade", "SGPA", "CGPA"});

            String[] courseCodes = {"ICT2113", "ICT2122", "ICT2133", "ICT2142", "ICT2152"};
            double[] credits = {3, 2, 3, 2, 2}; // Example credit values for each course
            double totalGradePoints = 0;
            double totalCredits = 0;

            while (rs.next()) {
                String courseCode = rs.getString("Course_code");

                double[] quizzes = {
                        rs.getDouble("Quiz_01"),
                        rs.getDouble("Quiz_02"),
                        rs.getDouble("Quiz_03"),
                        rs.getDouble("Quiz_04")
                };
                double[] assignments = {
                        rs.getDouble("Assignment_01"),
                        rs.getDouble("Assignment_02")
                };
                double[] endMarks = {
                        rs.getDouble("End_theory"),
                        rs.getDouble("End_practical")
                };

                Arrays.sort(quizzes);
                Arrays.sort(assignments);

                double caMark = 0, quizAvg = 0, assAvg = 0, mid = 0, mid1 = 0, endAvg = 0, finalMark = 0;

                switch (courseCode) {
                    case "ICT2113":
                        quizAvg = (quizzes[2] + quizzes[3]) / 2;
                        mid = rs.getDouble("Mid_theory");
                        mid1 = rs.getDouble("Mid_practical");
                        caMark = quizAvg * 0.1 + ((mid + mid1) * 0.1);
                        endAvg = (endMarks[0] * 0.1 * 4) + (endMarks[1] * 0.1 * 3);
                        finalMark = endAvg + caMark;
                        break;
                    case "ICT2122":
                        quizAvg = (quizzes[1] + quizzes[2] + quizzes[3]) / 3;
                        assAvg = assignments[0];
                        mid = rs.getDouble("Mid_theory");
                        mid1 = rs.getDouble("Mid_practical");
                        caMark = (quizAvg * 0.1) + (assAvg * 0.1) + ((mid + mid1) * 0.1);
                        endAvg = ((endMarks[0] * 0.1 * 6));
                        finalMark = endAvg + caMark;
                        break;
                    case "ICT2133":
                        quizAvg = (quizzes[2] + quizzes[3]) / 2;
                        assAvg = (assignments[0] + assignments[1]);
                        caMark = (quizAvg * 0.1) + (assAvg * 0.1);
                        endAvg = (endMarks[0] * 0.1 * 4) + (endMarks[1] * 0.1 * 3);
                        finalMark = endAvg + caMark;
                        break;
                    case "ICT2142":
                        assAvg = assignments[0] * 2;
                        mid = rs.getDouble("Mid_theory");
                        mid1 = rs.getDouble("Mid_practical");
                        caMark = (assAvg * 0.1) * ((mid + mid1) * 0.1);
                        endAvg = ((endMarks[1] * 0.1 * 6));
                        finalMark = endAvg + caMark;
                        break;
                    case "ICT2152":
                        quizAvg = (quizzes[2] + quizzes[3]) / 2;
                        assAvg = (assignments[0] + assignments[1]);
                        caMark = (quizAvg * 0.1) + (assAvg * 0.1);
                        endAvg = ((endMarks[0] * 0.1 * 7));
                        finalMark = endAvg + caMark;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid course code: " + courseCode);
                        continue;
                }

                String grade = getGrade(finalMark);
                double gradePoint = getGradePoint(grade);

                int courseIndex = Arrays.asList(courseCodes).indexOf(courseCode);
                if (courseIndex >= 0) {
                    totalGradePoints += gradePoint * credits[courseIndex];
                    totalCredits += credits[courseIndex];
                }
                model.addRow(new Object[]{index, courseCode, grade,"", ""});
            }

            double sgpa = totalGradePoints / totalCredits;
            double cgpa = sgpa;

            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(String.format("%.2f", sgpa), i, 3);
                model.setValueAt(String.format("%.2f", cgpa), i, 4);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data.");
        }
    }

    public String getGrade(double requiredPercentage) {
        if (requiredPercentage >= 80) {
            return "A+";
        } else if (requiredPercentage >= 75) {
            return "A";
        } else if (requiredPercentage >= 70) {
            return "A-";
        } else if (requiredPercentage >= 65) {
            return "B+";
        } else if (requiredPercentage >= 60) {
            return "B";
        } else if (requiredPercentage >= 55) {
            return "B-";
        } else if (requiredPercentage >= 50) {
            return "C+";
        } else if (requiredPercentage >= 45) {
            return "C";
        } else if (requiredPercentage >= 40) {
            return "D";
        } else {
            return "F";
        }
    }

    private double getGradePoint(String grade) {
        return switch (grade) {
            case "A+", "A" -> 4.0;
            case "A-" -> 3.7;
            case "B+" -> 3.3;
            case "B" -> 3.0;
            case "B-" -> 2.7;
            case "C+" -> 2.3;
            case "C" -> 2.0;
            case "D" -> 1.0;
            default -> 0.0;
        };

    }

    public static void main(String[] args) {
        new lec_ung_gpa();
    }
}
