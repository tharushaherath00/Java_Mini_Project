package UI;

import DAO.CourseDAO;
import utils.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CourseUI extends JFrame {
    private JLabel name;
    private JTextField courseName;
    private JLabel credit;
    private JTextField courseCredit;
    private JLabel type;
    private JTextField courseType;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private CourseDAO courseDAO = new CourseDAO();

    public CourseUI(){
        setTitle("Course Management");
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6,3));

        name = new JLabel("Course Name:");
        courseName = new JTextField(20);

        credit = new JLabel("Credit:");
        courseCredit = new JTextField(20);

        type = new JLabel("Course Type:");
        courseType = new JTextField(20);

        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");

        add(name);
        add(courseName);
        add(credit);
        add(courseCredit);
        add(type);
        add(courseType);
        add(addButton);
        add(deleteButton);
        add(updateButton);


        setVisible(true);

        addButton.addActionListener(e->addCourse());
        updateButton.addActionListener(e->updateCourse());



    }

    private void addCourse(){

        String name = courseName.getText().trim();
        String type = courseType.getText().trim();
        String credit = courseCredit.getText().trim();

        if(name.isEmpty()||type.isEmpty()||credit.isEmpty()){
            JOptionPane.showMessageDialog(this,"All fields must be filled!");
            return;
        }
        try{
            int c = Integer.parseInt(credit);
            int depId = 1;//default department
            courseDAO.addCourse(name,type,c,depId);
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Credit must be valid number");
        }catch (SQLException e){
            JOptionPane.showMessageDialog(this,"database Error");
        }

    }
    private void updateCourse(){
        String idInput;
        do{idInput = JOptionPane.showInputDialog(this,"Enter Course ID to Update");}
        while(idInput.trim().isEmpty());

        try{
            int courseId = Integer.parseInt(idInput.trim());
            String name = courseName.getText().trim();
            String type = courseType.getText().trim();
            String credit = courseCredit.getText().trim();

            if(name.isEmpty()||type.isEmpty()||credit.isEmpty()){
                JOptionPane.showMessageDialog(this,"All fields must be fill");
                return;
            }
            int c = Integer.parseInt(credit);
            courseDAO.updateCourse(courseId,name,type,c);
            JOptionPane.showMessageDialog(this,"Course Updated Successfully!");
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Please enter valid number for courseID and credit");
        }

    }

    public void deleteCourese(){
//        String idInput = JOptionPane.showInputDialog(this,"Enter coures id to delete");
        String idInput;
        do{idInput = JOptionPane.showInputDialog(this,"Enter Course ID to Update");}
        while(idInput.trim().isEmpty());

        try{
            int courseId = Integer.parseInt(idInput.trim());
            courseDAO.deleteCourse(courseId);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Please enter valid course Id");
            return;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Database error");
        }

    }

    public static void main(String[] args) {
        new CourseUI();
    }
}


