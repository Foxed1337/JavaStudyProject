import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private static Connection conn;
    public static Statement statmt;

    public static void Conn() throws ClassNotFoundException, SQLException {
        conn = null;
        statmt = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/TestDB.db");
        statmt = conn.createStatement();

        System.out.println("База Подключена!");
    }

    public static boolean DBisConnected() {
        return conn != null && statmt != null;
    }

    public static void CreateDB() throws ClassNotFoundException, SQLException {
        statmt.execute("PRAGMA foreign_keys=on");
        CreatePersonTable();
        CreateCoursesTable();
        CreateJavaCoursesTable();

        System.out.println("База данных успешно создана!");
    }

    private static void CreatePersonTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'Person' " +
                        "('person_id' INTEGER PRIMARY KEY, " +
                        "'vk_id' INTEGER, " +
                        "'name' text, " +
                        "'lastname' text, " +
                        "'home_town' text, " +
                        "'birthday' text, " +
                        "'student_id' INTEGER UNIQUE);"
        );

        System.out.println("Таблица Person создана или уже существует.");
    }

    private static void CreateCoursesTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'Courses' " +
                        "('student_id' INTEGER, " +
                        "'student_group' text, " +
                        "'course_name' text, " +
                        "'student_course_id' INTEGER PRIMARY KEY, " +
                        "FOREIGN KEY (student_id) REFERENCES Person (student_id));"
        );

        System.out.println("Таблица Courses создана или уже существует.");
    }

    private static void CreateJavaCoursesTable() throws SQLException {
        statmt.execute(
                "CREATE TABLE if not exists 'JavaCourse' " +
                        "('student_course_id' INT, " +
                        "'topic_name' text, " +
                        "'task_name' text, " +
                        "'mark' INT, " +
                        "'max_mark' INT, " +
                        "FOREIGN KEY (student_course_id) REFERENCES Courses (student_course_id));"
        );

        System.out.println("Таблица JavaCourse создана или уже существует.");
    }

    public static void WriteStudentToDB(Student st) throws SQLException {
        WritePersonData(st);
        WriteCoursesData(st);
    }

    private static void WritePersonData(Student st) throws SQLException {
        var command = String.format(
                "INSERT INTO 'Person' ('person_id', 'name', 'lastname', 'home_town', 'birthday', 'student_id', 'vk_id') " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s'); ",
                st.getPersonID(),
                st.getName(),
                st.getLastName(),
                st.getHomeTown(),
                st.getBirthday(),
                st.getPersonID(),
                st.getVK_ID());

        statmt.execute(command);
    }

    private static void WriteCoursesData(Student st) throws SQLException {
        int courseOffsetOfID = 0;
        for (var course : st.getCourses().values()) {
            var command = String.format(
                    "INSERT INTO 'Courses' ('student_id', 'course_name', 'student_course_id', 'student_group') " +
                            "VALUES ('%s', '%s', '%s', '%s');",
                    st.getPersonID(),
                    course.getName(),
                    st.getPersonID() + courseOffsetOfID,
                    course.getStudentGroup());

            statmt.execute(command);
            WriteJavaCourseData(course, st.getPersonID() + courseOffsetOfID);
            courseOffsetOfID++;
        }
    }

    private static void WriteJavaCourseData(Course course, int studentCourseId) throws SQLException {
        for (var topic : course.getAllTopics().values()) {
            for (var task : topic.getAllTask().values()) {
                var command = String.format(
                        "INSERT INTO 'JavaCourse' ('student_course_id','topic_name','task_name','mark','max_mark') " +
                                "VALUES ('%s', '%s', '%s','%s', '%s' );",
                        studentCourseId,
                        topic.getName(),
                        task.getName(),
                        task.getMark(),
                        task.getMAX_MARK());

                statmt.execute(command);
            }
        }
    }

    public static void ReadDB() throws ClassNotFoundException, SQLException {
        Statement statmt = conn.createStatement();
        ResultSet resSet = statmt.executeQuery("SELECT * FROM Person WHERE vk_id != 0");

        while (resSet.next()) {
            int id = resSet.getInt("vk_id");
            String name = resSet.getString("name");
            String phone = resSet.getString("lastname");
            System.out.println("ID = " + id);
            System.out.println("name = " + name);
            System.out.println("lastname = " + phone);
            System.out.println();
        }

        System.out.println("Таблица выведена");
    }

    public static int getMaxPersonID() throws SQLException {
        ResultSet resSet = statmt.executeQuery("SELECT MAX (person_id) FROM Person");
        if (resSet != null) {
            return resSet.getInt("MAX (person_id)");
        } else return 0;
    }

    //TODO Доделать
    public static void getStudentCourseMark(String name) throws SQLException {
        if (name == null || name.isEmpty()) {
            return;
        }
        var firstName = "";
        var lastName = "";
        var fullName = name.split(" ");
        if (fullName.length == 1) {
            firstName = fullName[0];
        } else {
            firstName = fullName[0];
            lastName = fullName[1];
        }
        ResultSet resSet = statmt.executeQuery("SELECT MAX (person_id) FROM Person");
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException {
        statmt.close();
        conn.close();
        System.out.println("Соединения закрыты");
    }
}
