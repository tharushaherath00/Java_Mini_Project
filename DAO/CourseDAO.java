package DAO;

import Models.Course;
import utils.DBConnection;

import javax.swing.plaf.basic.BasicDesktopIconUI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public void addCourse(String courseName,String courseType,int credit,int departmentId)throws SQLException{

        String sql = "INSERT INTO course(course_name,course_type,credit,department_id)VALUES(?,?,?,?)";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
        ){
            stmt.setString(1,courseName);
            stmt.setString(2,courseType);
            stmt.setInt(3,credit);
            stmt.setInt(4,departmentId);
            stmt.executeUpdate();
//            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }



    }

    public void deleteCourse(int courseId)throws SQLException{
        String sql = "DELETE FROM course WHERE course_id = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            stmt.setInt(1,courseId);
            stmt.executeUpdate();
//            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateCourse(int courseId,String courseName,String courseType,int credit){
        String sql = "UPDATE course SET course_name=?,course_type=?,credit=?WHERE course_id=?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            stmt.setString(1,courseName);
            stmt.setString(2,courseType);
            stmt.setInt(3,credit);
            stmt.setInt(4,courseId);
            stmt.executeUpdate();
//            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

//    public List<Course> getAllCourses()throws Exception{
//        String sql = "SELECT * FROM course";
//        List<Course> course = new ArrayList<>();
//        try(Connection connection = DBConnection.getConnection();){
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(sql);
//
//
//            while(rs.next()){
//                course.add(new Course(
//                        rs.getInt("course_id"),
//                        rs.getString("course_name"),
//                        rs.getString("course_type"),
//                        rs.getInt("credit")// ❌ Extra comma here
//                ));
//
//
//            }
//
//
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//
//        return course;
//
//    }

    public List<Course> getAllCourses() throws SQLException {
        String sql = "SELECT * FROM course";
        List<Course> courses = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("course_type"),
                        rs.getInt("department_id"),
                        rs.getInt("credit")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

}
