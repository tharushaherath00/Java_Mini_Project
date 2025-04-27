package org.example;

import java.sql.Time;

public class Timetable {
    private int timetableId;
    private String courseId;
    private String day;
    private Time startTime;
    private Time endTime;
    private String room;
    private String lecturerId;

    public Timetable(int timetableId, String courseId, String day, Time startTime, Time endTime, String room, String lecturerId) {
        this.timetableId = timetableId;
        this.courseId = courseId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.lecturerId = lecturerId;
    }

    public int getTimetableId() { return timetableId; }
    public void setTimetableId(int timetableId) { this.timetableId = timetableId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }
    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getLecturerId() { return lecturerId; }
    public void setLecturerId(String lecturerId) { this.lecturerId = lecturerId; }
}