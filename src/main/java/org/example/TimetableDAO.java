package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {
    public static void addTimetable(Timetable timetable) throws SQLException {
        if (hasConflict(timetable, 0)) {
            throw new SQLException("Scheduling conflict detected: Room or lecturer is already booked.");
        }

        String sql = "INSERT INTO Timetable (Timetable_ID, Course_ID, Day, Start_Time, End_Time, Room, Lecturer_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, timetable.getTimetableId());
            stmt.setString(2, timetable.getCourseId());
            stmt.setString(3, timetable.getDay());
            stmt.setTime(4, timetable.getStartTime());
            stmt.setTime(5, timetable.getEndTime());
            stmt.setString(6, timetable.getRoom());
            stmt.setString(7, timetable.getLecturerId());
            stmt.executeUpdate();
        }
    }

    public static List<Timetable> getAllTimetables(String departmentId) {
        List<Timetable> timetables = new ArrayList<>();
        String sql = "SELECT t.* FROM Timetable t " +
                "JOIN Course c ON t.Course_ID = c.Course_ID " +
                "WHERE c.Dep_ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Timetable timetable = new Timetable(
                            rs.getInt("Timetable_ID"),
                            rs.getString("Course_ID"),
                            rs.getString("Day"),
                            rs.getTime("Start_Time"),
                            rs.getTime("End_Time"),
                            rs.getString("Room"),
                            rs.getString("Lecturer_ID")
                    );
                    timetables.add(timetable);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timetables;
    }

    public static void updateTimetable(Timetable timetable) throws SQLException {
        if (hasConflict(timetable, timetable.getTimetableId())) {
            throw new SQLException("Scheduling conflict detected: Room or lecturer is already booked.");
        }

        String sql = "UPDATE Timetable SET Course_ID = ?, Day = ?, Start_Time = ?, End_Time = ?, Room = ?, Lecturer_ID = ? WHERE Timetable_ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, timetable.getCourseId());
            stmt.setString(2, timetable.getDay());
            stmt.setTime(3, timetable.getStartTime());
            stmt.setTime(4, timetable.getEndTime());
            stmt.setString(5, timetable.getRoom());
            stmt.setString(6, timetable.getLecturerId());
            stmt.setInt(7, timetable.getTimetableId());
            stmt.executeUpdate();
        }
    }

    public static void deleteTimetable(int timetableId) throws SQLException {
        String sql = "DELETE FROM Timetable WHERE Timetable_ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, timetableId);
            stmt.executeUpdate();
        }
    }

    private static boolean hasConflict(Timetable newTimetable, int excludeId) throws SQLException {
        String sql = "SELECT * FROM Timetable WHERE Day = ? AND Timetable_ID != ? AND " +
                "((Start_Time <= ? AND End_Time >= ?) OR (Start_Time <= ? AND End_Time >= ?) OR " +
                "(Start_Time >= ? AND End_Time <= ?)) AND (Room = ? OR Lecturer_ID = ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTimetable.getDay());
            stmt.setInt(2, excludeId);
            stmt.setTime(3, newTimetable.getEndTime());
            stmt.setTime(4, newTimetable.getStartTime());
            stmt.setTime(5, newTimetable.getStartTime());
            stmt.setTime(6, newTimetable.getEndTime());
            stmt.setTime(7, newTimetable.getStartTime());
            stmt.setTime(8, newTimetable.getEndTime());
            stmt.setString(9, newTimetable.getRoom());
            stmt.setString(10, newTimetable.getLecturerId());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static List<String> getAvailableCourses(String departmentId) {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT Course_ID FROM Course WHERE Dep_ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(rs.getString("Course_ID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static List<String> getAvailableLecturers(String departmentId) {
        List<String> lecturers = new ArrayList<>();
        String sql = "SELECT Lecturer_ID FROM Lecturer WHERE Dep_ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lecturers.add(rs.getString("Lecturer_ID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturers;
    }

    public static int getNextTimetableId() {
        String sql = "SELECT MAX(Timetable_ID) + 1 AS next_id FROM Timetable";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
}