package org.example;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBConnector {
    private static MongoClient mongoClient;
    public static MongoDatabase mongoDatabase;

    // Singleton method to initialize MongoDB connection
    public static void connect() {
        if (mongoClient == null) {
            mongoClient = new MongoClient("localhost", 27017);
            MongoCredential mongoCredential = MongoCredential.createCredential("Krithik", "TeacherStudentPortal", "root123".toCharArray());
            mongoDatabase = mongoClient.getDatabase("TeacherStudentPortal");
            System.out.println("Connected to the database successfully");
            System.out.println("mongoCredential = " + mongoCredential);
            System.out.println("Database Name = " + mongoDatabase.getName());
        }
    }

    // Method to close MongoDB connection
    public static void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            System.out.println("Disconnected from the database.");
        }
    }
}
