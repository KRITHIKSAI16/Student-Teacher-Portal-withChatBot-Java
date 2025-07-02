package org.example;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseID;
    private String courseName;
    private String teacherID;
    private List<String> studentUsernames;

    public Course(String courseID, String courseName,String teacherName) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.teacherID =teacherName;
        this.studentUsernames = new ArrayList<>();
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacherID(){
        return teacherID;
    }

    public List<String> getStudentIDs() {
        return studentUsernames;
    }

    public void setStudentIDs(List<String> studentIDs) {
        this.studentUsernames = studentIDs;
    }

    public void addStudentUsernames(String studentID) {
        this.studentUsernames.add(studentID);
    }

}