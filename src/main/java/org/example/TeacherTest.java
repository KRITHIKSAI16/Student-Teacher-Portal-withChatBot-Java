package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class TeacherTest {
    public static void main(String[] args) {
        // Setup MongoDB connection and get Teacher collection
        MongoDBConnector.connect();  // Assuming MongoDBConnector is set up with the database connection
        MongoCollection<Document> teacherCollection = MongoDBConnector.mongoDatabase.getCollection("Teacher");

        // Specify the username of the teacher to test retrieval
        String testUsername = "Vanitha";  // Replace with an actual username in your database

        // Retrieve teacher document from MongoDB based on the username
        Document teacherDoc = teacherCollection.find(Filters.eq("username", testUsername)).first();

        if (teacherDoc != null) {
            // Convert Document to Teacher object
            Teacher teacher = Teacher.fromDocument(teacherDoc);

            // Print the teacher's details to verify object creation
            System.out.println("Teacher Username: " + teacher.getUsername());
            System.out.println("Teacher Password: " + teacher.getPassword());
            System.out.println("Courses Taught:");

            for (Course course : teacher.getCourses()) {
                System.out.println(" - Course ID: " + course.getCourseID());
                System.out.println("   Course Name: " + course.getCourseName());
                System.out.println("   Teacher ID: " + course.getTeacherID());
                System.out.println("   Enrolled Students: " + course.getStudentIDs());
            }
        } else {
            System.out.println("Teacher with username '" + testUsername + "' not found in the database.");
        }

        MongoDBConnector.disconnect();
    }

}
