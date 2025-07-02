# Student-Teacher-Portal-withChatBot-Java

This repository contains the code  for a **Student-Teacher Portal**, developed using **Java** with **MongoDB** for data persistence. The project includes separate login and registration for students and teachers, along with assignment posting, attendance tracking, to-do list management, and a chatbot feature for interactive PDF-based question-answering.

> 💬 The chatbot functionality is powered by **Ollama**, a locally running LLM.

---

## Key Features

###  Teacher Module
- Register and log in as a teacher
- Post assignments with deadlines
- Mark attendance for enrolled students
- View individual student attendance records

###  Student Module
- Register and log in as a student
- View assignments
- View personal attendance
- Maintain a to-do list and mark task completion
- Upload a PDF and ask questions using a chatbot (Ollama-powered)

---

##  Tech Stack

| Component      | Description                                       |
|----------------|---------------------------------------------------|
| Java           | Core backend logic                                |
| MongoDB        | NoSQL database for storing teacher & student data |
| MongoDB Compass| GUI to visualize and manage MongoDB collections   |
| Ollama         | Local LLM for chatbot-based PDF Q&A               |

---

##  Project Structure 
```
📁 StudentTeacherPortal/
├── src/
│   └── main/
│       └── java/
│           └── org/example/
│               ├── Main.java
│               ├── Mainmenu.java
│               ├── MongoDBConnector.java
│               ├── Student.java
│               ├── Teacher.java
│               ├── Assignment.java
│               ├── Assignments.java
│               ├── ToDoList.java
│               ├── Task.java
│               ├── Chatbot.java
│               ├── PDFTextExtractor.java
│               ├── studentMenu.java
│               ├── teacherMenu.java
│               ├── UsernameAlreadyExistsException.java
│               └── UsernameNotFoundException.java
├── pom.xml
└── README.md
```

---

##  MongoDB Collections

- `TeacherStudentPortal`
  - `Student` – stores username, password, attendance, courses, todo, assignments
  - `Teacher` – stores username, password, posted assignments, etc.
  - `Courses` – stores course-related info

Make sure MongoDB is running on `localhost:27017`.

---

##  How to Run the Project

### 1. Clone the Repository
```bash
git clone https://github.com/KRITHIKSAI16/Student-Teacher-Portal-withChatBot-Java
cd Student-Teacher-Portal-withChatBot-Java
```

2. Start MongoDB
Make sure MongoDB is installed and running locally on port 27017.

3. Install Ollama
Install Ollama from https://ollama.com and run a model like llama3 or mistral:
```bash
ollama run llama3
```

4. Compile and Run
```bash
javac src/main/java/org/example/Mainmenu.java
java src/main/java/org/example/Mainmenu
```

Author
Krithik Sai S







