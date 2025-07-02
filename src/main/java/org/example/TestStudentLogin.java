package org.example;

import java.util.Map;

public class TestStudentLogin {
    public static void main(Student student) {
        // Call the loginStudent method which now creates and returns a Student object
         // Assume this method now returns the student object

        // Temporary test code: Display loaded student data if the student object is not null
        if (student != null) {
            System.out.println("Test: Student object loaded successfully.");
            System.out.println("Username: " + student.getUsername());

            System.out.println("\nToDo List - Not Completed Tasks:");
            for (Task task : student.getToDoList().getNotCompleted()) {
                System.out.println("Task: " + task.getTaskDesc() + ", Due Date: " + task.getDueDate());
            }

            System.out.println("\nToDo List - Completed Tasks:");
            for (Task task : student.getToDoList().getCompleted()) {
                System.out.println("Task: " + task.getTaskDesc() + ", Due Date: " + task.getDueDate());
            }

            System.out.println("\nAssignments - Not Completed:");
            for (Assignment assignment : student.getAssignments().getNotCompleted()) {
                System.out.println("Course ID: " + assignment.getCourseID() + ", Description: " + assignment.getAssignmentDesc());
            }

            System.out.println("\nAssignments - Completed:");
            for (Assignment assignment : student.getAssignments().getCompleted()) {
                System.out.println("Course ID: " + assignment.getCourseID() + ", Description: " + assignment.getAssignmentDesc());
            }

            System.out.println("\nAttendance:");
            for (Map.Entry<String, Map<String, Integer>> entry : student.getAttendance().entrySet()) {
                String courseId = entry.getKey();
                Map<String, Integer> attendanceRecords = entry.getValue();

                // Iterate through the attendance records for the specific course
                for (Map.Entry<String, Integer> record : attendanceRecords.entrySet()) {
                    System.out.println("Course ID: " + courseId + ", Student ID: " + record.getKey() + ", Attendance Days: " + record.getValue());
                }
            }

            System.out.println("\nCourses Enrolled:");
            for (Course course : student.getCourses()) {
                System.out.println("Course ID: " + course.getCourseID() + ", Course Name: " + course.getCourseName());
            }
        } else {
            System.out.println("Test Failed: Student object was not created or loaded correctly.");
        }
    }
}
