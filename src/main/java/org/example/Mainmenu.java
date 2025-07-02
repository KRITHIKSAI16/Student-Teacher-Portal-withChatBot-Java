package org.example;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import com.mongodb.client.model.Filters;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mainmenu {
    public static void main(String[] args) {
        MongoDBConnector.connect();
        System.out.println("-----TEACHER STUDENT PORTAL-----");

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println(ConsoleColors.BLUE+"Select an Option\n" +
                    "1-->Register as Teacher\n" +
                    "2-->Login as Teacher\n" +
                    "3-->Register as Student\n" +
                    "4-->Login as Student\n" +
                    "5-->Exit"+ConsoleColors.RESET);


            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    registerTeacher(); // Call registerTeacher method
                    break;
                case 2:
                    loginTeacher(sc); // Pass scanner to loginTeacher
                    break;
                case 3:
                    registerStudent(); // Call registerStudent method
                    break;
                case 4:
                    loginStudent(sc); // Call loginStudent method
                    break;
                case 5:
                    System.out.println("Exiting the portal. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }

        } while (choice != 5);

        sc.close();
        MongoDBConnector.disconnect();
    }

    public static void registerTeacher() {
        System.out.println("Registering as Teacher...");
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password;

        // Connect to MongoDB
        MongoCollection<Document> teacherCollection = MongoDBConnector.mongoDatabase.getCollection("Teacher");
        MongoCollection<Document> coursesCollection = MongoDBConnector.mongoDatabase.getCollection("Courses");

        // Check for unique username
        boolean isUnique = false;
        while (!isUnique) {
            try {
                System.out.println("Enter your Username:");
                username = sc.nextLine();

                // Check if username already exists in the collection
                Document existingTeacher = teacherCollection.find(Filters.eq("username", username)).first();
                if (existingTeacher != null) {
                    throw new UsernameAlreadyExistsException("Username already exists. Please enter a unique username.");
                } else {
                    isUnique = true;
                }
            } catch (UsernameAlreadyExistsException e) {
                System.out.println(e.getMessage());
                System.out.println("Would you like to try another username? (yes/no)");
                String choice = sc.nextLine().trim().toLowerCase();
                if (!choice.equals("yes")) {
                    System.out.println("Registration cancelled.");
                    return; // Exit the method if the user doesn't want to try again
                }
            }
        }


        System.out.println("Enter your 4 digit teacherID - ");
        long tid = sc.nextLong();

        sc.nextLine();

        System.out.println("Enter your Password:");
        password = sc.nextLine();

        // Create a new Teacher object
        Teacher newTeacher = new Teacher(username, password, tid);

        // Get the courses the teacher teaches
        System.out.println("Enter the Number of courses you Teach:");
        int n = sc.nextInt();
        sc.nextLine(); // Consume the newline

        for (int i = 0; i < n; i++) {
            String courseID, courseName;
            System.out.println("Enter Course ID:");
            courseID = sc.nextLine();
            System.out.println("Enter Course Name:");
            courseName = sc.nextLine();

            // Add the course to the teacher's course list
            newTeacher.addCourse(courseID, courseName);

            // Insert course in Courses collection
            Document newCourse = new Document("courseID", courseID)
                    .append("courseName", courseName)
                    .append("teacherID", username)
                    .append("studentUsernames", new ArrayList<String>()); // Initialize with an empty list of student usernames
            coursesCollection.insertOne(newCourse);
        }

        // Insert teacher into the Teacher collection
        teacherCollection.insertOne(newTeacher.toDocument());
        System.out.println(ConsoleColors.BRIGHT_GREEN+"YOU HAVE BEEN REGISTERED SUCCESSFULLY"+ConsoleColors.RESET);

    }

    public static void loginTeacher(Scanner sc) {
        boolean authenticated = false;
        sc.nextLine();

        MongoCollection<Document> teacherCollection = MongoDBConnector.mongoDatabase.getCollection("Teacher");

        String username = "";
        String password;

        while (true) {
            try {
                System.out.println("Enter your Username:");
                username = sc.nextLine();

                // Check if username exists
                Document existingTeacher = teacherCollection.find(Filters.eq("username", username)).first();
                if (existingTeacher == null) {
                    throw new UsernameNotFoundException("USERNAME NOT FOUND\nPlease register if you have not already.");
                }
                break; // Exit loop if username is found

            } catch (UsernameNotFoundException e) {
                System.out.println(e.getMessage());
                System.out.println("Press 1 to try again or 0 to go back to the main menu.");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline
                if (choice == 0) {
                    return; // Exit and go back to the main menu
                }
            }
        }

        while (!authenticated) {
            System.out.println("Enter your Password:");
            password = sc.nextLine();

            // Retrieve the stored password from MongoDB
            Document existingTeacher = teacherCollection.find(Filters.eq("username", username)).first();
            String storedPassword = existingTeacher.getString("password");

            // Check if password matches
            if (storedPassword.equals(password)) {
                authenticated = true;
                System.out.println(ConsoleColors.BRIGHT_GREEN+"Login successful! Welcome, " + username+ConsoleColors.RESET);

                // Create a Teacher object from the retrieved data
                Teacher teacher = Teacher.fromDocument(existingTeacher);

                // Pass the Teacher object to the teacherMenu main method
                teacherMenu.main(teacher, sc);

            } else {
                System.out.println("Invalid password. Please try again.");
                System.out.println("Press 1 to try again or 0 to go back to the main menu.");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline
                if (choice == 0) {
                    return; // Exit and go back to the main menu
                }
            }
        }
    }


    public static void registerStudent() {

        System.out.println("Registering as Student...");
        Scanner sc = new Scanner(System.in);
        long registrationNumber = 0;
        String username = "";
        String password;

        // Connect to MongoDB
        MongoCollection<Document> studentCollection = MongoDBConnector.mongoDatabase.getCollection("Student");
        MongoCollection<Document> coursesCollection = MongoDBConnector.mongoDatabase.getCollection("Courses");
        MongoCollection<Document> teacherCollection = MongoDBConnector.mongoDatabase.getCollection("Teacher");

        // Check for unique username
        boolean isUnique = false;
        while (!isUnique) {
            try {
                System.out.println("Enter your Username:");
                username = sc.nextLine();

                // Check if username already exists in the collection
                Document existingStudent = studentCollection.find(Filters.eq("username", username)).first();
                if (existingStudent != null) {
                    throw new UsernameAlreadyExistsException("Username already exists. Please enter a unique username.");
                } else {
                    isUnique = true;
                }
            } catch (UsernameAlreadyExistsException e) {
                System.out.println(e.getMessage());
                System.out.println("Would you like to try another username? (yes/no)");
                String choice = sc.nextLine().trim().toLowerCase();
                if (!choice.equals("yes")) {
                    System.out.println("Registration cancelled.");
                    return; // Exit the method if the user doesn't want to try again
                }
            }
        }


        System.out.println("Enter your Registration Number - ");
        registrationNumber = sc.nextLong();

        sc.nextLine();

        System.out.println("Enter your Password:");
        password = sc.nextLine();

        // Create new student object
        Student newStudent = new Student(username, password, registrationNumber);

        // Fetch available courses and display them
        List<Document> courseDocs = coursesCollection.find().into(new ArrayList<>());
        System.out.println("Available Courses:");

        for (Document courseDoc : courseDocs) {
            String courseID = courseDoc.getString("courseID");
            String courseName = courseDoc.getString("courseName");
            String teacherID = courseDoc.getString("teacherID");
            System.out.println("Course ID: " + courseID + ", Course Name: " + courseName + ", Teacher: " + teacherID);
        }

        // Ask the student how many courses they want to enroll in
        System.out.println("Enter the number of courses you want to enroll in:");
        int numberOfCourses = sc.nextInt();
        sc.nextLine(); // Consume the newline

        for (int i = 0; i < numberOfCourses; i++) {
            System.out.println("Enter the Course ID and Teacher Username (separated by a space):");
            String input = sc.nextLine();
            String[] parts = input.split(" ");

            if (parts.length != 2) {
                System.out.println("Invalid input. Please enter both Course ID and Teacher Username.");
                i--; // Repeat this iteration
                continue;
            }

            String courseID = parts[0];
            String teacherUsername = parts[1];

            // Retrieve the selected course document from the courses collection
            Document courseDoc = coursesCollection.find(Filters.and(Filters.eq("courseID", courseID), Filters.eq("teacherID", teacherUsername))).first();
            if (courseDoc != null) {
                String courseName = courseDoc.getString("courseName");

                // Create a Course object with the complete information
                Course selectedCourse = new Course(courseID, courseName, teacherUsername);

                // Add the course to the student's course list
                newStudent.getCourses().add(selectedCourse);

                // Update the course's student list in the Courses collection
                coursesCollection.updateOne(
                        Filters.and(Filters.eq("courseID", courseID), Filters.eq("teacherID", teacherUsername)),
                        Updates.addToSet("studentUsernames", username)
                );

                // Update the teacher's course list in the Teacher collection
                teacherCollection.updateOne(
                        Filters.and(Filters.eq("username", teacherUsername), Filters.eq("courses.courseID", courseID)),
                        Updates.addToSet("courses.$.studentUsernames", username)
                );

            } else {
                System.out.println("Course with the specified ID and Teacher Username not found.");
                i--; // Repeat this iteration if invalid course selection
            }
        }

        // Insert the new student into the Student collection
        studentCollection.insertOne(newStudent.toDocument());
        System.out.println(ConsoleColors.BRIGHT_GREEN+"YOU HAVE BEEN REGISTERED SUCCESSFULLY"+ConsoleColors.RESET);
    }

    public static void loginStudent(Scanner sc) {
        sc.nextLine(); // Consume the newline
        MongoCollection<Document> studentCollection = MongoDBConnector.mongoDatabase.getCollection("Student");
        String username, password;
        Document existingStudent = null;
        boolean authenticated = false;
        boolean exists = false;

        while (!exists) {
            System.out.println("Enter your Username:");
            username = sc.nextLine();

            // Check if username exists in the collection
            existingStudent = studentCollection.find(Filters.eq("username", username)).first();

            if (existingStudent == null) {
                System.out.println("USERNAME NOT FOUND\nPlease register if you have not already.");
                System.out.println("Press 1 to try again or 0 to go back to the main menu.");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline
                if (choice == 0) {
                    return; // Go back to main menu
                }
            } else {
                exists = true; // Username found, exit the loop
            }
        }

        // Password validation
        while (!authenticated) {
            System.out.println("Enter your Password:");
            password = sc.nextLine();

            // Check stored password
            String storedPassword = existingStudent.getString("password");
            if (storedPassword.equals(password)) {
                authenticated = true;
                System.out.println(ConsoleColors.BRIGHT_GREEN + "Login successful! Welcome, " +   ConsoleColors.RESET);

                // Create and pass Student object to studentMenu
                Student student = Student.fromDocument(existingStudent);
                studentMenu.main(student, sc);

                System.out.println("Returning to the main menu...");  // Confirms that logout is completed
                return; // End `loginStudent` to prevent re-entry
            } else {
                System.out.println("Invalid password. Please try again.");
                System.out.println("Press 1 to try again or 0 to go back to the main menu.");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline
                if (choice == 0) {
                    return; // Go back to main menu
                }
            }
        }
    }


}
