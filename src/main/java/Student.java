import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Student {

    private String name;
    private String surname;

    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public static void main(String[] args) {


        List<String> surnames = new ArrayList<>();
        List<Student> italianStudents = new ArrayList<>();
        List<Student> germanStudents  = new ArrayList<>();

        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:33061/newdb","developer","123456");

            Statement statement = connection.createStatement();

            //Drop table and view
            String query0 = "DROP TABLE IF EXISTS `students`;";
            statement.execute(query0);
            String query01 = "DROP VIEW IF EXISTS `italian_students`;";
            statement.execute(query01);
            String query02 = "DROP VIEW IF EXISTS `german_students`;";
            statement.execute(query02);

            //Create a table students if not exists
            String query = "CREATE TABLE IF NOT EXISTS students("
                    + "Student_id INT NOT NULL AUTO_INCREMENT, "
                    + "first_name VARCHAR (20) NOT NULL, "
                    + "last_name VARCHAR (20) NOT NULL, "
                    + "PRIMARY KEY (Student_id))";
            //Execute query
            statement.execute(query);
            System.out.println("The table was created!");

            //Insert 4 record with name and surname
            System.out.println("Inserting records into the table");

            String sql = "INSERT INTO students VALUES (1, 'Bruce', 'Banner')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO students VALUES (2, 'Peter', 'Parker')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO students VALUES (3, 'Bruce', 'Wayne')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO students VALUES (4, 'Tony', 'Stark')";
            statement.executeUpdate(sql);

            System.out.println("Done!");


            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");
            System.out.println("Query for extract name and surname:");
                while (resultSet.next()){
                    surnames.add(resultSet.getString("last_name"));
                    System.out.println(resultSet.getString("first_name") + " - " +  resultSet.getString("last_name"));
                }

            //alter table, add new colummn "country"
            String query1 = "ALTER TABLE students ADD country  CHAR(30)";

            statement.execute(query1);
            System.out.println("Country column added to the table");

            System.out.println("Update students nationality...");
            String query2 = "UPDATE students " +
                    "SET country = 'Italy' WHERE student_id in (1, 2)";
            statement.executeUpdate(query2);

            String query3 = "UPDATE students " +
                    "SET country = 'Germany' WHERE student_id in (3, 4)";
            statement.executeUpdate(query3);
            System.out.println("Done!");

            //create view italian_students and german_students
            String setView1 = "CREATE VIEW italian_students AS (SELECT * from students WHERE country = 'Italy');";
            String setView2 = "CREATE VIEW german_students AS (SELECT * from students WHERE country = 'Germany');";

            statement.executeUpdate(setView1);
            statement.executeUpdate(setView2);


            ResultSet resultSet2 = statement.executeQuery("SELECT * FROM italian_students;");
            //ResultSet resultSet3 = statement.executeQuery("SELECT * FROM german_students;");

            //System.out.println("Query for extract name and surname from VIEW:");
            //Student student1 = new Student(resultSet2.getString("first_name"),resultSet2.getString("last_name"));

            System.out.println("Query for extract name and surname from VIEW italian_students:");
            while (resultSet2.next()){
                //italianStudents.add(student1);
                System.out.println(resultSet2.getString("first_name") + " - " +  resultSet2.getString("last_name"));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("-----------------------------------");
        System.out.println("All surname from arrayList 'surnames':");
        System.out.println(surnames);

        System.out.println("-----------------------------------");
        System.out.println("All surname from arrayList 'italian_students':");
        System.out.println(italianStudents);

    }
}
