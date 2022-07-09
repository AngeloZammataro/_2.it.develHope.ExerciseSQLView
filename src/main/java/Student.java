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

        Student studentA = new Student("Anna","Neri");
        Student studentB = new Student("Marco","Rossi");
        Student studentC = new Student("Benjamin","Schneider");
        Student studentD = new Student("Gerald","Weber");

        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:33061/newdb","developer","123456");

            Statement statement = connection.createStatement();

            //Drop table and view for test
            String query0 = "DROP TABLE IF EXISTS `students`;";
            statement.execute(query0);
            String query01 = "DROP VIEW IF EXISTS `italian_students`;";
            statement.execute(query01);
            String query02 = "DROP VIEW IF EXISTS `german_students`;";
            statement.execute(query02);

            //Create a table students
            String query = "CREATE TABLE students("
                    + "Student_id INT NOT NULL AUTO_INCREMENT, "
                    + "first_name VARCHAR (20) NOT NULL, "
                    + "last_name VARCHAR (20) NOT NULL, "
                    + "PRIMARY KEY (Student_id))";
            //Execute query
            statement.execute(query);
            System.out.println("The table was created!");

            //Insert 4 record with name and surname for students

            System.out.println("Inserting records into the table");

            //prepare String with placeholder
            String sqlX = "INSERT INTO students(first_name,last_name) "
                    + "VALUES(?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlX,
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, studentA.getName());
            preparedStatement.setString(2, studentA.getSurname());
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, studentB.getName());
            preparedStatement.setString(2, studentB.getSurname());
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, studentC.getName());
            preparedStatement.setString(2, studentC.getSurname());
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, studentD.getName());
            preparedStatement.setString(2, studentD.getSurname());
            preparedStatement.executeUpdate();
/*
            String sql = "INSERT INTO students VALUES (1, 'Anna', 'Neri')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO students VALUES (2, 'Marco', 'Rossi')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO students VALUES (3, 'Benjamin', 'Schneider')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO students VALUES (4, 'Gerald', 'Weber ')";
            statement.executeUpdate(sql);
*/
            System.out.println("Done!");

            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");
            System.out.println("Query for extract name and surname:");
                while (resultSet.next()){
                    surnames.add(resultSet.getString("last_name"));
                    System.out.println(resultSet.getString("first_name") + " - " +  resultSet.getString("last_name"));
                }
            System.out.println("Done!");

            //alter table, add new colummn "country"
            String query1 = "ALTER TABLE students ADD country  VARCHAR(30)";

            statement.execute(query1);
            System.out.println("Country column added to the table");

            System.out.println("Update students nationality...");
            String query2 = "UPDATE students " +
                    "SET country = 'Italy' WHERE student_id IN (1, 2)";
            statement.executeUpdate(query2);

            String query3 = "UPDATE students " +
                    "SET country = 'Germany' WHERE student_id IN (3, 4)";
            statement.executeUpdate(query3);
            System.out.println("Done!");

            //create view italian_students
            String setViewItaStd = "CREATE VIEW italian_students AS (SELECT * FROM students WHERE country = 'Italy');";
            statement.executeUpdate(setViewItaStd);

            ResultSet resultSetItaStd = statement.executeQuery("SELECT * FROM italian_students;");

            System.out.println("Query for extract name and surname from VIEW italian_students:");
            while (resultSetItaStd.next()){
                Student student1FromDb = new Student(resultSetItaStd.getString("first_name"),resultSetItaStd.getString("last_name"));
                italianStudents.add(student1FromDb);
                System.out.println(resultSetItaStd.getString("first_name") + " - " +  resultSetItaStd.getString("last_name"));
            }

            //create view german_students
            String setViewGerStd = "CREATE VIEW german_students AS (SELECT * FROM students WHERE country = 'Germany');";
            statement.executeUpdate(setViewGerStd);

            ResultSet resultSetGerStd = statement.executeQuery("SELECT * FROM german_students;");
            System.out.println("Query for extract name and surname from VIEW german_students:");
            while (resultSetGerStd.next()){
                Student student2FromDb = new Student(resultSetGerStd.getString("first_name"),resultSetGerStd.getString("last_name"));
                germanStudents.add(student2FromDb);
                System.out.println(resultSetGerStd.getString("first_name") + " - " +  resultSetGerStd.getString("last_name"));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("-----------------------------------");
        System.out.println("All surname from arrayList 'surnames':");
        System.out.println(surnames);

        System.out.println("-----------------------------------");
        System.out.println("All surname from arrayList 'italian_students':");
        italianStudents.forEach(student -> System.out.println(student.getName() + " - " + student.getSurname()));

        System.out.println("-----------------------------------");
        System.out.println("All surname from arrayList 'german_students':");
        germanStudents.forEach(student -> System.out.println(student.getName() + " - " + student.getSurname()));

    }
}
