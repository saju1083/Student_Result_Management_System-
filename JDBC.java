package jdbc; 
import java.sql.*;
import java.util.*;

public class JDBC{
    static final String URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root";
    static final String PASS = ""; 

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<Integer, Integer[]> studentMarksMap = new HashMap<>();

        try {
            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();
            sc.nextLine(); 

            System.out.print("Enter Student Name: ");
            String name = sc.nextLine();

            System.out.println("Enter Marks for 3 Subjects:");
            Integer[] marks = new Integer[3];
            int total = 0;
            for (int i = 0; i < 3; i++) {
                System.out.print("Subject " + (i + 1) + ": ");
                marks[i] = sc.nextInt();
                total += marks[i];
            }

            studentMarksMap.put(id, marks);

            String grade = calculateGrade(total / 3);

            insertData(id, name, total, grade);

            displayRecords();

        } catch (Exception e) {
            System.out.println("Error in Input: " + e.getMessage());
        }
    }

    public static String calculateGrade(int avg) {
        if (avg >= 80) return "A+";
        else if (avg >= 70) return "A";
        else if (avg >= 60) return "A-";
        else if (avg >= 50) return "B";
        else return "F";
    }

    public static void insertData(int id, String name, int total, String grade) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "INSERT INTO students (id, name, total, grade) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, total);
            pstmt.setString(4, grade);
            pstmt.executeUpdate();
            System.out.println("\n✅ Data inserted into database successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
        }
    }

    public static void displayRecords() {
        System.out.println("\n--- Current Student Records in Database ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                                   " | Name: " + rs.getString("name") + 
                                   " | Total: " + rs.getInt("total") + 
                                   " | Grade: " + rs.getString("grade"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching data: " + e.getMessage());
        }
    }
}