package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.example.Student.updateStudentInDatabase;

public class teacherMenu {
    public static void main(Teacher teacher,Scanner sc) {
        int choice;
        System.out.println("WELCOME TO YOUR TEACHER DASHBOARD");

        do {
            System.out.println(ConsoleColors.BRIGHT_YELLOW+"Select an Option\n" +
                    "1-->POST ASSIGNMENT\n" +
                    "2-->ATTENDANCE\n" +
                    "3-->LOGOUT"+ConsoleColors.RESET);

            choice=sc.nextInt();

            switch (choice) {
                case 1:
                    postAssignment(teacher, sc);
                    break;
                case 2:
                    attendance(teacher, sc);
                    break;
                case 3:
                    logoutTeacher(teacher);
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }

        } while (choice != 3);
    }


    public static void postAssignment(Teacher teacher,Scanner sc) {
        sc.nextLine();
        MongoCollection<Document> studentCollection = MongoDBConnector.mongoDatabase.getCollection("Student");

        // Show the courses the teacher teaches
        System.out.println("Courses you teach:");
        for (Course course : teacher.getCourses()) {
            System.out.println(" - Course ID: " + course.getCourseID() + ", Course Name: " + course.getCourseName());
        }

        // Ask for courseID, description, and due date
        System.out.print("Enter the Course ID for the assignment: ");
        String courseID = sc.nextLine();

        // Check if the teacher teaches this course
        boolean isCourseValid = teacher.getCourses().stream().anyMatch(course -> course.getCourseID().equals(courseID));
        if (!isCourseValid) {
            System.out.println("Invalid Course ID. Please select a valid course ID.");
            return;
        }

        System.out.print("Enter the assignment description: ");
        String assignmentDesc = sc.nextLine();

        System.out.print("Enter the due date (format: YYYY-MM-DD): ");
        String dueDate = sc.nextLine();

        // Create the assignment
        Assignment assignment = new Assignment(courseID, assignmentDesc, dueDate);

        // Find all students enrolled in this course and add the assignment to their notCompleted list
        List<Document> studentsEnrolled = studentCollection.find(Filters.eq("courses.courseID", courseID)).into(new ArrayList<>());

        for (Document studentDoc : studentsEnrolled) {
            // Add assignment to the student's notCompleted list
            Document assignmentDoc = new Document("courseID", assignment.getCourseID())
                    .append("assignmentDesc", assignment.getAssignmentDesc())
                    .append("dueDate", assignment.getDueDate());

            // Update the student's notCompleted assignments list
            studentCollection.updateOne(
                    Filters.eq("username", studentDoc.getString("username")),
                    Updates.push("assignments.notCompleted", assignmentDoc)
            );
        }

        System.out.println(ConsoleColors.BRIGHT_GREEN+"Assignment posted successfully for course ID: " + courseID+ConsoleColors.RESET);
    }



    public static void logoutTeacher(Teacher teacher) {
        MongoCollection<Document> teacherCollection = MongoDBConnector.mongoDatabase.getCollection("Teacher");

        // Convert the Teacher object to a MongoDB document
        Document updatedTeacherDoc = teacher.toDocument();

        // Update the MongoDB entry with the new document using the teacher's username as the filter
        teacherCollection.replaceOne(Filters.eq("username", teacher.getUsername()), updatedTeacherDoc);

        System.out.println("Teacher data successfully updated in MongoDB.");


    }


    public static void attendance(Teacher teacher, Scanner sc) {
        int attendanceChoice;

        do {
            System.out.println(ConsoleColors.BRIGHT_YELLOW+"1--> Take Attendance\n2--> View Attendance\n3--> Go Back to Dashboard"+ConsoleColors.RESET);
            attendanceChoice = sc.nextInt();

            switch (attendanceChoice) {
                case 1:
                    takeAttendance(teacher, sc);
                    break;
                case 2:
                    viewAttendance(teacher, sc);
                    break;
                case 3:
                    System.out.println("Returning to dashboard...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        } while (attendanceChoice != 3); // Loop until they choose to go back
    }


    private static void takeAttendance(Teacher teacher, Scanner sc) {
        System.out.println("Select a course to take attendance for:");

        // Display list of courses the teacher teaches
        for (int i = 0; i < teacher.getCourses().size(); i++) {
            Course course = teacher.getCourses().get(i);
            System.out.println((i + 1) + "--> " + course.getCourseID() + ": " + course.getCourseName());
        }

        int courseIndex = sc.nextInt() - 1;
        if (courseIndex < 0 || courseIndex >= teacher.getCourses().size()) {
            System.out.println("Invalid course selection.");
            return;
        }

        Course selectedCourse = teacher.getCourses().get(courseIndex);

        // Ask for date input
        System.out.println("Enter the date for attendance (YYYY-MM-DD):");
        String date = sc.next();

        // Fetch students in the selected course
        List<String> studentUsernames = selectedCourse.getStudentIDs();
        MongoCollection<Document> studentCollection = MongoDBConnector.mongoDatabase.getCollection("Student");

        for (String username : studentUsernames) {
            System.out.println("Enter attendance for " + username + " (1 for present, 0 for absent): ");
            int attendance = sc.nextInt();

            // Update attendance in MongoDB
            Document studentDoc = studentCollection.find(Filters.eq("username", username)).first();
            if (studentDoc != null) {
                Document attendanceDoc = (Document) studentDoc.get("attendance");
                if (attendanceDoc == null) {
                    attendanceDoc = new Document();
                }

                Document courseAttendance = (Document) attendanceDoc.get(selectedCourse.getCourseID());
                if (courseAttendance == null) {
                    courseAttendance = new Document();
                }

                courseAttendance.put(date, attendance);
                attendanceDoc.put(selectedCourse.getCourseID(), courseAttendance);
                studentCollection.updateOne(Filters.eq("username", username), new Document("$set", new Document("attendance", attendanceDoc)));
            }
        }
        System.out.println("Attendance successfully recorded for " + selectedCourse.getCourseName() + ".");
    }

    private static void viewAttendance(Teacher teacher, Scanner sc) {
        System.out.println("Select a course to view attendance:");

        // Display list of courses the teacher teaches
        for (int i = 0; i < teacher.getCourses().size(); i++) {
            Course course = teacher.getCourses().get(i);
            System.out.println((i + 1) + "--> " + course.getCourseID() + ": " + course.getCourseName());
        }

        int courseIndex = sc.nextInt() - 1;
        if (courseIndex < 0 || courseIndex >= teacher.getCourses().size()) {
            System.out.println("Invalid course selection.");
            return;
        }

        Course selectedCourse = teacher.getCourses().get(courseIndex);
        MongoCollection<Document> studentCollection = MongoDBConnector.mongoDatabase.getCollection("Student");

        System.out.println("Attendance for " + selectedCourse.getCourseName() + ":\n");

        // Calculate and display attendance for each student in the selected course
        for (String username : selectedCourse.getStudentIDs()) {
            Document studentDoc = studentCollection.find(Filters.eq("username", username)).first();
            if (studentDoc != null) {
                Map<String, Integer> courseAttendance = ((Document) studentDoc.get("attendance"))
                        .get(selectedCourse.getCourseID(), Document.class)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> (Integer) entry.getValue()));

                int totalClasses = courseAttendance.size();
                long presentCount = courseAttendance.values().stream().filter(att -> att == 1).count();
                double attendancePercentage = (double) presentCount / totalClasses * 100;

                System.out.printf("%s: %.2f%% attendance (%d/%d)\n", username, attendancePercentage, presentCount, totalClasses);
            }
        }
    }
}



