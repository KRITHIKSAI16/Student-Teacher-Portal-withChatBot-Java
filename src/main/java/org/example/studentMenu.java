package org.example;

import opennlp.tools.parser.Cons;

import java.util.Map;
import java.util.Scanner;

import static org.example.Student.updateStudentInDatabase;

public class studentMenu {
    public static void main(Student student,Scanner sc) {
        int choice=0;
        System.out.println(ConsoleColors.BRIGHT_PURPLE+"WELCOME TO YOUR DASHBOARD"+ConsoleColors.RESET);


        do{

            System.out.println(ConsoleColors.BRIGHT_YELLOW+"Select a Option\n" +
                    "1-->TO DO LIST\n"+
                    "2-->VIEW ASSIGNMENTS\n"+
                    "3-->VIEW ATTENDANCE\n"+"4-->CHATBOT\n"+"5-->Logout"+ConsoleColors.RESET);

            choice=sc.nextInt();

            switch (choice) {
                case 1:
                    toDoList(student,sc); // Call the registerTeacher method
                    break;
                case 2:
                    viewAssignments(student,sc); // Call the loginTeacher method
                    break;
                case 3:
                    viewAttendance(student,sc); // Call the registerStudent method
                    break;
                case 4:
                    askDoubt(sc); // Call the loginStudent method
                    break;
                case 5:
                    logoutStudent(student);
                    return;

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }

        }while(choice!=5);

    }

    public static void toDoList(Student student, Scanner sc){
        int choice = 0;
        ToDoList todoList = student.getToDoList();

        // ANSI color codes for output
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";

        while (choice != 5) {
            System.out.println("\n" + ANSI_YELLOW + "----- TO-DO LIST MENU -----" + ANSI_RESET);
            System.out.println("1 --> View Non-Completed Tasks");
            System.out.println("2 --> View Completed Tasks");
            System.out.println("3 --> Add a New Task");
            System.out.println("4 --> Mark a Task as Completed");
            System.out.println("5 --> Return to Dashboard");

            choice = sc.nextInt();
            sc.nextLine();  // Consume newline left-over

            switch (choice) {
                case 1:
                    // View Non-Completed Tasks
                    System.out.println("\n" + ANSI_YELLOW + "Non-Completed Tasks:" + ANSI_RESET);
                    if (todoList.getNotCompleted().isEmpty()) {
                        System.out.println("No non-completed tasks available.");
                    } else {
                        int index = 1;
                        for (Task task : todoList.getNotCompleted()) {
                            System.out.println(index + ". " + task.getTaskDesc() + " (Due: " + task.getDueDate() + ")");
                            index++;
                        }
                    }
                    break;

                case 2:
                    // View Completed Tasks
                    System.out.println("\n" + ANSI_GREEN + "Completed Tasks:" + ANSI_RESET);
                    if (todoList.getCompleted().isEmpty()) {
                        System.out.println("No completed tasks available.");
                    } else {
                        int index = 1;
                        for (Task task : todoList.getCompleted()) {
                            System.out.println(index + ". " + task.getTaskDesc() + " (Completed on: " + task.getDueDate() + ")");
                            index++;
                        }
                    }
                    break;

                case 3:
                    // Add a New Task
                    System.out.print("Enter task description: ");
                    String description = sc.nextLine();
                    System.out.print("Enter due date (e.g., YYYY-MM-DD): ");
                    String dueDate = sc.nextLine();

                    // Determine the next task number
                    int taskNo = todoList.getNotCompleted().size() + todoList.getCompleted().size() + 1;
                    Task newTask = new Task(taskNo, description, dueDate);
                    todoList.addTask(newTask);

                    System.out.println("New task added successfully!");
                    student.setTodoList(todoList);  // Update the student's to-do list
                    updateStudentInDatabase(student);  // Save to the database
                    break;

                case 4:
                    // Mark a Task as Completed
                    System.out.println("Select a task number to mark as completed:");
                    if (todoList.getNotCompleted().isEmpty()) {
                        System.out.println("No non-completed tasks available.");
                    } else {
                        int taskIndex = 1;
                        for (Task task : todoList.getNotCompleted()) {
                            System.out.println(taskIndex + ". " + task.getTaskDesc() + " (Due: " + task.getDueDate() + ")");
                            taskIndex++;
                        }
                        int selectedTask = sc.nextInt();
                        sc.nextLine();  // Consume newline left-over

                        if (selectedTask > 0 && selectedTask <= todoList.getNotCompleted().size()) {
                            Task completedTask = todoList.getNotCompleted().get(selectedTask - 1);
                            todoList.completeTask(completedTask);

                            System.out.println("Task marked as completed successfully!");
                            student.setTodoList(todoList);  // Update the student's to-do list
                            updateStudentInDatabase(student);  // Save to the database
                        } else {
                            System.out.println("Invalid task number. Please try again.");
                        }
                    }
                    break;

                case 5:
                    System.out.println("Returning to dashboard...");
                    break;

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }

    }

    public static void logoutStudent(Student student) {
        // Update the student in MongoDB before logging out
        updateStudentInDatabase(student);

        // Proceed with logout actions
        System.out.println("Logging out. Goodbye!");

        return;
        // Add any other necessary logout logic here
    }

    public static void viewAssignments(Student student,Scanner sc){
        // ANSI color codes
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";

        System.out.println(ANSI_RED + "----- VIEW ASSIGNMENTS -----" + ANSI_RESET);

        Assignments assignments = student.getAssignments();

        // Display non-completed assignments
        System.out.println(ANSI_RED + "Non-Completed Assignments:" + ANSI_RESET);
        int index = 1;
        for (Assignment assignment : assignments.getNotCompleted()) {
            System.out.println(index + ". Course ID: " + assignment.getCourseID());
            System.out.println("   Course Name: " + getCourseNameByID(student, assignment.getCourseID()));
            System.out.println("   Description: " + assignment.getAssignmentDesc());
            System.out.println("   Due Date: " + assignment.getDueDate());
            index++;
        }

        // Display completed assignments
        System.out.println("\n" + ANSI_GREEN + "Completed Assignments:" + ANSI_RESET);
        for (Assignment assignment : assignments.getCompleted()) {
            System.out.println("Course ID: " + assignment.getCourseID());
            System.out.println("Course Name: " + getCourseNameByID(student, assignment.getCourseID()));
            System.out.println("Description: " + assignment.getAssignmentDesc());
            System.out.println("Due Date: " + assignment.getDueDate());
        }

        // Ask the student if they want to mark an assignment as completed
        System.out.println("\nDo you want to mark any assignment as completed? (Enter assignment number or 0 to go back)");
        int choice = sc.nextInt();
        if (choice > 0 && choice <= assignments.getNotCompleted().size()) {
            Assignment assignmentToComplete = assignments.getNotCompleted().get(choice - 1);
            assignments.completeAssignment(assignmentToComplete);

            System.out.println("Assignment marked as completed successfully!");

            // Update the database with the modified assignment status
            student.setAssignments(assignments);
            updateStudentInDatabase(student); // Update the student's assignments in the database
        } else {
            System.out.println("Returning to dashboard...");
        }

    }

    private static String getCourseNameByID(Student student, String courseID) {
        for (Course course : student.getCourses()) {
            if (course.getCourseID().equals(courseID)) {
                return course.getCourseName();
            }
        }
        return "Unknown Course";
    }

    public static void viewAttendance(Student student, Scanner sc) {
        System.out.println("Attendance Summary:");

        // Loop through each course the student is enrolled in
        for (Map.Entry<String, Map<String, Integer>> entry : student.getAttendance().entrySet()) {
            String courseID = entry.getKey();
            Map<String, Integer> attendanceRecords = entry.getValue();

            // Retrieve course details
            String courseName = ""; // Default in case the course is not found
            for (Course course : student.getCourses()) {
                if (course.getCourseID().equals(courseID)) {
                    courseName = course.getCourseName();
                    break;
                }
            }

            // Calculate attendance percentage
            int totalClasses = attendanceRecords.size();
            int attendedClasses = (int) attendanceRecords.values().stream().filter(value -> value == 1).count();
            double attendancePercentage = ((double) attendedClasses / totalClasses) * 100;

            // Display course details with course name and code
            System.out.print("Course: " + courseID + " (" + courseName + ") - ");

            // Check attendance percentage and color the output accordingly
            if (attendancePercentage < 75) {
                System.out.print("\u001B[31m" + String.format("%.2f", attendancePercentage) + "%\u001B[0m"); // Red for < 75%

                // Calculate and display the additional classes required for 75%
                int neededClasses = (int) Math.ceil(0.75 * totalClasses) - attendedClasses;
                System.out.println(" (Attend " + neededClasses + " more classes to reach 75%)");
            } else {
                System.out.println("\u001B[32m" + String.format("%.2f", attendancePercentage) + "%\u001B[0m"); // Green for >= 75%
            }
        }

        System.out.println(); // Empty line for formatting
    }

    public static void askDoubt(Scanner sc) {
        System.out.println(ConsoleColors.BRIGHT_CYAN+"Select an Option\n1--> Ask a General Doubt\n2--> Ask Doubt about a PDF"+ConsoleColors.RESET);
        int doubtChoice = sc.nextInt();
        sc.nextLine();  // Consume the newline

        switch (doubtChoice) {
            case 1:
                // General doubt
                System.out.print("Enter your question: ");
                String generalDoubt = sc.nextLine();
                String answer = Chatbot.askGeneralDoubt(generalDoubt);
                System.out.println("Answer: " + answer);
                break;

            case 2:
                // PDF-based doubt
                System.out.print("Enter the PDF path: ");
                String pdfPath = sc.nextLine();
                System.out.print("Enter your question related to the PDF: ");
                String pdfDoubt = sc.nextLine();
                String pdfAnswer = Chatbot.askDoubtAboutPDF(pdfPath, pdfDoubt);
                System.out.println("Answer: " + pdfAnswer);
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }





}
