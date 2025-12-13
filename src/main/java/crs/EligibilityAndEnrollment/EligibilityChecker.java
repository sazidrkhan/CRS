package crs.EligibilityAndEnrollment;

import java.io.*;
import java.util.*;

public class EligibilityChecker {
    
    private ArrayList<Student> students;
    private ArrayList<Course> courses;
    private HashMap<String, ArrayList<StudentCourseRecord>> studentRecords;
    
    public EligibilityChecker() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        studentRecords = new HashMap<>();
        loadData();
    }
    
    private void loadData() {
        loadStudents();
        loadCourses();
        loadStudentRecords();
    }
    
private void loadStudents() {

    final String STUDENT_FILE = "data/students-abood.txt"; 
    
    try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
        String line;
        boolean firstLine = true;
        
        while ((line = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue; 
            }
             line = line.trim();
            if (line.isEmpty()) continue; 
            
            String[] data = line.split(",", -1);
            
       
            if (data.length >= 7) { 
                
                Student student = new Student(
                    data[0].trim(), // StudentID
                    data[1].trim(), // FirstName
                    data[2].trim(), // LastName (can be empty)
                    data[3].trim(), // Major
                    data[4].trim(), // Courses (New field)
                    data[5].trim(), // Year
                    data[6].trim()  // Email
                );
                students.add(student);
            } else {
                
                System.out.println("Skipping malformed student data line (expected 7 fields, got " + data.length + "): " + line);
            }
        }
    } catch (IOException e) {
        System.out.println("Error loading students: " + e.getMessage());
    }
}
    
    private void loadCourses() {
try (BufferedReader br = new BufferedReader(new FileReader("data/courses.txt"))) {
        String line;
        boolean firstLine = true;
        while ((line = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue;
            }
            String[] data = line.split(",");
            if (data.length < 8) continue;


            String major = data[0].trim();
            String courseID = data[1].trim();
            String courseName = data[2].trim();
            int creditHour = Integer.parseInt(data[3].trim());
            String semester = data[4].trim(); // Changed 'intake' variable to 'semester' for clarity
            String instructor = data[5].trim(); // Changed 'lecturer' variable to 'instructor' for clarity
            int assignmentWeight = Integer.parseInt(data[6].trim());
            int examWeight = Integer.parseInt(data[7].trim());


            Course course = new Course(
                courseID,             // 1. Course ID (from data[1])
                courseName,           // 2. Course Name (from data[2])
                semester,             // 3. Semester (from data[4])
                creditHour,           // 4. Credits (from data[3])
                instructor,           // 5. Instructor (from data[5])
                instructor,           // 6. Lecturer (from data[5]) - Assuming Instructor/Lecturer are the same for this file
                examWeight,           // 7. Exam Weight (from data[7])
                assignmentWeight      // 8. Assignment Weight (from data[6])
            );
            
            courses.add(course);
        }
    } catch (IOException e) {
        System.out.println("Error loading courses: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Error parsing numeric values in courses.txt: " + e.getMessage());
    }
}
    
private void loadStudentRecords() {
   
    try (BufferedReader br = new BufferedReader(new FileReader("data/GradedStudentRecords.txt"))) {
        String line;
        boolean firstLine = true;
        
        while ((line = br.readLine()) != null) {
            
           
            line = line.trim();
            if (line.isEmpty()) {
                continue; 
            }
            
            if (firstLine) {
                firstLine = false;
                continue; 
            }
            
            
            String[] data = line.split(",");
            
        
            if (data.length < 4) { 
                System.out.println("Skipping malformed record (expected 4+ fields, got " + data.length + "): " + line);
                continue; 
            }

            
            String studentId = data[0].trim();
            String courseId = data[1].trim();
            
        
            try {
                double examScore = Double.parseDouble(data[2].trim());
                double assignmentScore = Double.parseDouble(data[3].trim());
                
                String grade = "";
                if (data.length >= 6) { // StudentID,CourseID,Exam,Assign,GPA,Grade
                    grade = data[5].trim();
                }
                
                StudentCourseRecord record = new StudentCourseRecord(studentId, courseId, examScore, assignmentScore, grade);
                
                if (!studentRecords.containsKey(studentId)) {
                    studentRecords.put(studentId, new ArrayList<>());
                }
                studentRecords.get(studentId).add(record);
                
            } catch (NumberFormatException e) {
                
                System.out.println("Error parsing scores for record: " + line + ". Details: " + e.getMessage());
                
            }
        }
    } catch (IOException e) {
        System.out.println("Error loading student records: " + e.getMessage());
    }
}
    
    public double calculateCGPA(String studentId) {
        if (!studentRecords.containsKey(studentId)) {
            return 0.0;
        }
        
        ArrayList<StudentCourseRecord> records = studentRecords.get(studentId);
        double totalGradePoints = 0;
        int totalCredits = 0;
        
        for (StudentCourseRecord record : records) {
            Course course = getCourseById(record.getCourseId());
            if (course != null) {
                double finalScore = record.calculateFinalScore(course);
                double gradePoint = convertScoreToGradePoint(finalScore);
                totalGradePoints += gradePoint * course.getCredits();
                totalCredits += course.getCredits();
            }
        }
        
        if (totalCredits == 0) {
            return 0.0;
        }
        
        return totalGradePoints / totalCredits;
    }
    
    public int countFailedCourses(String studentId) { 
        if (!studentRecords.containsKey(studentId)) {
            return 0;
        }
        
        ArrayList<StudentCourseRecord> records = studentRecords.get(studentId);
        int failedCount = 0;
        
        for (StudentCourseRecord record : records) {
            Course course = getCourseById(record.getCourseId());
            if (course != null) {
                double finalScore = record.calculateFinalScore(course);
                if (finalScore < 50) {
                    failedCount++;
                }
            }
        }
        
        return failedCount;
    }
    
    
    public boolean saveStudentRecord(String studentId, String courseId, double examScore, double assignmentScore) {
    ArrayList<StudentCourseRecord> records = studentRecords.getOrDefault(studentId, new ArrayList<>());
    boolean updated = false;

    // Update existing record if exists
    for (StudentCourseRecord r : records) {
        if (r.getCourseId().equals(courseId)) {
            r.examScore = examScore;
            r.assignmentScore = assignmentScore;
            updated = true;
            break;
        }
    }

    // If no record exists, create new
    if (!updated) {
        records.add(new StudentCourseRecord(studentId, courseId, examScore, assignmentScore));
        studentRecords.put(studentId, records);
    }

    // Write all records back to file
    try (PrintWriter pw = new PrintWriter(new FileWriter("data/GradedStudentRecords.txt"))) {
        pw.println("StudentID,CourseID,ExamScore,AssignmentScore"); // header
        for (String sid : studentRecords.keySet()) {
            for (StudentCourseRecord r : studentRecords.get(sid)) {
                pw.println(r.getStudentId() + "," + r.getCourseId() + "," + r.getExamScore() + "," + r.getAssignmentScore());
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
    return true;
}
    
    
    
    
    public boolean isEligible(String studentId) {
     double cgpa = calculateCGPA(studentId);
     int failedCourses = countFailedCourses(studentId);
    
    // CGPA >= 2.0 AND Failed Courses < 3 (not <= 3)
    return (cgpa >= 2.0) && (failedCourses < 3);
    }
    
    public ArrayList<Student> getAllStudents() {
         
        return students;
    }
    
    public ArrayList<StudentCourseRecord> getStudentRecords(String studentId) {
        return studentRecords.getOrDefault(studentId, new ArrayList<>());
    }
    
    public ArrayList<Course> getAllCourses() {
        return courses;
    }
    
 public Student getStudentById(String studentId) {
    for (Student student : students) {
        if (student.getStudentID().equals(studentId)) {
            return student;
        }
    }
    return null;
}
    
    private Course getCourseById(String courseId) {
        for (Course course : courses) {
            if (course.getCourseID().equals(courseId)) {
                return course;
            }
        }
        return null;
    }
    
    private double convertScoreToGradePoint(double score) {
        if (score >= 90) return 4.0;
        else if (score >= 85) return 3.7;
        else if (score >= 80) return 3.3;
        else if (score >= 75) return 3.0;
        else if (score >= 70) return 2.7;
        else if (score >= 65) return 2.3;
        else if (score >= 60) return 2.0;
        else if (score >= 55) return 1.7;
        else if (score >= 50) return 1.0;
        else return 0.0;
    }

    public Student findStudent(String searchKey) {
        if (searchKey == null || searchKey.isEmpty()) {
            return null; 
        }
        ArrayList<Student> students = getAllStudents();
        
        for (Student student : students) {
            if (student.getStudentID().equalsIgnoreCase(searchKey) ||
                student.getFullName().toLowerCase().contains(searchKey.toLowerCase())) {
                return student;
            }
        }
        return null;
    }

    public Course getCourseByIdPublic(String courseCode) { //big problem with part3Recovery
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public class StudentCourseRecord {
        private String studentId;
        private String courseId;
        private double examScore;
        private double assignmentScore;
        private String grade; 
        
        public StudentCourseRecord(String studentId, String courseId, double examScore, double assignmentScore) {
            this.studentId = studentId;
            this.courseId = courseId;
            this.examScore = examScore;
            this.assignmentScore = assignmentScore;
        }
        
        
        //overloaded constructor which is needed in RecoveryPlanManager
        public StudentCourseRecord(String studentId, String courseId,
                           double examScore, double assignmentScore,
                           String grade) {
            this(studentId, courseId, examScore, assignmentScore);
            this.grade = grade;
        }

        
        public double calculateFinalScore(Course course) {
            return (examScore * course.getExamWeight() / 100.0) + 
                   (assignmentScore * course.getAssignmentWeight() / 100.0);
        }
        
        public String getStudentId() {
            return studentId;
        }
        
        public String getCourseId() {
            return courseId;
        }
        
        public double getExamScore() {
            return examScore;
        }
        
        public double getAssignmentScore() {
            return assignmentScore;
        }
        
        public String getGrade() {
            return grade;
        }

    }
}