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

    public static boolean isConnected() {
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
                        "'firstname' text, " +
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
                "INSERT INTO 'Person' ('person_id', 'firstname', 'lastname', 'home_town', 'birthday', 'student_id', 'vk_id') " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s'); ",
                st.getPersonID(),
                st.getFirstname(),
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

    public static int getMaxPersonID() throws SQLException {
        ResultSet resSet = statmt.executeQuery("SELECT MAX (person_id) FROM Person");
        if (resSet != null) {
            return resSet.getInt("MAX (person_id)");
        } else return 0;
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException {
        statmt.close();
        conn.close();
        System.out.println("Соединения закрыты");
    }

    //Запросы
    //public static void getStudentInfo(String name) {
    //}

    public static String getPersonInfo(String name) throws SQLException {
        var parsedName = getParsedName(name);
        if (parsedName == null) {
            return "Имя введено некорректно";
        }
        var firstName = parsedName[0];
        var lastName = parsedName[1];
        var command = String.format(
                "SELECT * FROM Person WHERE firstname = \"%s\" AND lastname = \"%s\" ",
                firstName, lastName);

        ResultSet resSet = statmt.executeQuery(command);
        if (resSet.isClosed()) {
            return String.format("Не удалось найти студента: %s %s", firstName, lastName);
        }

        int id = resSet.getInt("vk_id");
        String firstname = resSet.getString("firstname");
        String lastname = resSet.getString("lastname");
        String birthday = resSet.getString("birthday");
        String homeTown = resSet.getString("home_town");

        return String.format(
                "Имя: %s\nФамилия: %s\nДень рождения: %s\nГород: %s\nВК: %s\n",
                firstname, lastname, birthday, homeTown, id
        );
    }

    private static String[] getParsedName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        var firstName = "";
        var lastName = "";
        var fullName = name.split(" ");
        if (fullName.length == 1) {
            firstName = toUpperCaseFirstLetter(fullName[0]);
        } else {
            firstName = toUpperCaseFirstLetter(fullName[0]);
            lastName = toUpperCaseFirstLetter(fullName[1]);
        }

        return new String[]{firstName, lastName};
    }

    private static String toUpperCaseFirstLetter(String str) {
        StringBuilder strB = new StringBuilder(str);
        if (Character.isAlphabetic(str.codePointAt(0)))
            strB.setCharAt(0, Character.toUpperCase(str.charAt(0)));
        return strB.toString();
    }

    public static String getAllStudents() throws SQLException {
        ResultSet resSet = statmt.executeQuery("SELECT firstname, lastname FROM 'Person'");
        if (resSet.isClosed()) {
            return "Не удалось получть список студентов";
        }

        var strB = new StringBuilder();
        strB.append(String.format("Имя          Фамилия\n-----------------------\n"));
        var spaces = "             ";
        while (resSet.next()) {
            var firstname = resSet.getString("firstname");
            var lastname = resSet.getString("lastname");
            var needSpaceLength = spaces.length() - firstname.length();
            strB.append(String.format("%s%s%s\n", firstname, spaces.substring(0, needSpaceLength), lastname));
        }

        return strB.toString();
    }
    //TODO переделать в один запрос к БД
    public static String getStudentCourses(String name) throws SQLException {
        var parsedName = getParsedName(name);
        var firstName = parsedName[0];
        var lastName = parsedName[1];
        var command = String.format(
                "SELECT student_id FROM Person WHERE firstname = \"%s\" AND lastname = \"%s\" ",
                firstName, lastName);
        ResultSet resSet = statmt.executeQuery(command);

        if (resSet.isClosed()) {
            return "Нет информации об курсах данного студента";
        }

        int student_id = resSet.getInt("student_id");

        command = String.format(
                "SELECT student_group, course_name, student_course_id FROM Courses WHERE student_id = \"%s\"  ",
                student_id);
        resSet = statmt.executeQuery(command);

        var strB = new StringBuilder();
        strB.append("Курсы студента:\n\n");

        while (resSet.next()) {
            var group = resSet.getString("student_group");
            var course = resSet.getString("course_name");
            strB.append(String.format("Курс: %s\nГруппа: %s\n", course, group));
        }
        return strB.toString();
    }
    //TODO переделать в один запрос к БД
    public static String getStudentCourseProgress(String name, String course) throws SQLException {
        var parsedName = getParsedName(name);
        var firstName = parsedName[0];
        var lastName = parsedName[1];
        var command = String.format(
                "SELECT student_id FROM Person WHERE firstname = \"%s\" AND lastname = \"%s\" ",
                firstName, lastName);
        ResultSet resSet = statmt.executeQuery(command);

        if (resSet.isClosed()) {
            return "Нет информации об оценках данного студента";
        }

        int student_id = resSet.getInt("student_id");

        command = String.format(
                "SELECT student_course_id FROM Courses WHERE student_id = \"%s\"  ",
                student_id);
        resSet = statmt.executeQuery(command);
        int student_course_id = resSet.getInt("student_course_id");


        return "";
    }
}


