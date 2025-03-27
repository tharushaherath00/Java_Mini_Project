package Models;

public class Course {
    private int courseId;
    private String courseName;
    private String courseType;
    private int credit;
    private int departmentId;

//    constructors,getters


    public Course() {
    }


    public Course(int courseId, String courseName, String courseType, int credit, int departmentId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseType = courseType;
        this.credit = credit;
        this.departmentId = departmentId;
    }

    public int getCourseId(){
        return courseId;
    }

    public String getCourseName(){
        return courseName;
    }

    public String getCourseType(){
        return courseType;
    }


}
