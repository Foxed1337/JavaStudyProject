import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class DBManager {

    private static Connection conn;
    public static Statement statmt;

    public static void Conn() throws ClassNotFoundException, SQLException {
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
                        "'gender' text, " +
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
                        "'max_score' INTEGER, " +
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
                "INSERT INTO 'Person' ('person_id', 'firstname', 'lastname', 'home_town', 'birthday', 'student_id', 'vk_id' , 'gender') " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'); ",
                st.getPersonID(),
                st.getFirstname(),
                st.getLastName(),
                st.getHomeTown(),
                st.getBirthday(),
                st.getPersonID(),
                st.getVK_ID(),
                st.getGender());

        statmt.execute(command);
    }

    private static void WriteCoursesData(Student st) throws SQLException {
        int courseOffsetOfID = 0;
        for (var course : st.getCourses().values()) {
            var command = String.format(
                    "INSERT INTO 'Courses' ('student_id', 'course_name', 'student_course_id', 'student_group', 'max_score') " +
                            "VALUES ('%s', '%s', '%s', '%s', '%s');",
                    st.getPersonID(),
                    course.getName(),
                    st.getPersonID() + courseOffsetOfID,
                    course.getStudentGroup(),
                    course.getMaxScore());

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
        if (!resSet.isClosed()) {
            return resSet.getInt("MAX (person_id)");
        } else return 0;
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException {
        statmt.close();
        conn.close();
        System.out.println("Соединения закрыты");
    }

    public static String getPersonInfo(String name) throws SQLException {
        var parsedName = getParsedName(name);
        if (parsedName == null) {
            return null;
        }
        var firstName = parsedName[0];
        var lastName = parsedName[1];
        var command = String.format(
                "SELECT * FROM Person WHERE firstname = \"%s\" AND lastname = \"%s\" ",
                firstName, lastName);

        ResultSet resSet = statmt.executeQuery(command);
        if (resSet.isClosed()) {
            return null;
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
        var lowerStr = str.toLowerCase(Locale.ROOT);
        StringBuilder strB = new StringBuilder(lowerStr);
        if (Character.isAlphabetic(lowerStr.codePointAt(0)))
            strB.setCharAt(0, Character.toUpperCase(lowerStr.charAt(0)));
        return strB.toString();
    }

    public static String getAllStudents() throws SQLException {
        ResultSet resSet = statmt.executeQuery("SELECT firstname, lastname FROM 'Person'");
        if (resSet.isClosed()) {
            return null;
        }

        var strB = new StringBuilder();
        strB.append("Имя          Фамилия\n-----------------------\n");
        while (resSet.next()) {
            var firstname = resSet.getString("firstname");
            var lastname = resSet.getString("lastname");
            var needSpaceLength = 13 - firstname.length();
            strB.append(String.format("%s%s%s\n", firstname, spaceCreator(needSpaceLength), lastname));
        }

        return strB.toString();
    }

    public static String getStudentCourses(String name) throws SQLException {
        var parsedName = getParsedName(name);
        var firstName = parsedName[0];
        var lastName = parsedName[1];
        var command = String.format(
                "SELECT Person.student_id,Courses.student_group, Courses.course_name, Courses.student_course_id " +
                        "FROM Person,Courses " +
                        "WHERE Person.firstname = \"%s\" AND Person.lastname = \"%s\" AND  Courses.student_id = Person.student_id",
                firstName, lastName);
        ResultSet resSet = statmt.executeQuery(command);

        if (resSet.isClosed()) {
            return null;
        }

        var strB = new StringBuilder();
        strB.append("Курсы студента:\n\n");

        while (resSet.next()) {
            var group = resSet.getString("student_group");
            var course = resSet.getString("course_name");
            strB.append(String.format("Курс: %s\nГруппа: %s\n", course, group));
        }
        return strB.toString();
    }

    public static String getStudentCourseProgress(String name, String course) throws SQLException {
        var parsedName = getParsedName(name);
        var firstName = parsedName[0];
        var lastName = parsedName[1];
        var command = String.format(
                "SELECT Courses.max_score,Courses.student_course_id,JavaCourse.topic_name, JavaCourse.task_name, JavaCourse.mark, JavaCourse.max_mark " +
                        "FROM Person, Courses, JavaCourse " +
                        "WHERE Person.firstname = \"%s\" AND Person.lastname = \"%s\" AND  Courses.student_id = Person.student_id " +
                        "AND Courses.student_course_id = JavaCourse.student_course_id ",
                firstName, lastName);
        var lastTopic = "";
        var strB = new StringBuilder();
        var score = getStudentScore(name, course);
        ResultSet resSet = statmt.executeQuery(command);
        if (resSet.isClosed()) {
            return null;
        }
        var maxScore = resSet.getInt("max_score");
        strB.append(String.format("Курс пройден на %s%%", (int) (score / (double) maxScore * 100)));
        while (resSet.next()) {
            var currentTopic = resSet.getString("topic_name");
            if (!currentTopic.equals(lastTopic)) {
                lastTopic = currentTopic;
                var needSpaceLength = 48 - lastTopic.length();
                strB.append(String.format("\n%s:%s%s \n\n", lastTopic, spaceCreator(needSpaceLength), "Балл:"));
            }
            var taskName = resSet.getString("task_name");
            var mark = resSet.getInt("mark");
            var needSpaceLength = 42 - taskName.length();
            strB.append(String.format("\t\t%s:%s%s\n", taskName, spaceCreator(needSpaceLength), mark));
        }
        /*
        Все оценки
        SELECT Courses.student_course_id,JavaCourse.topic_name, JavaCourse.task_name, JavaCourse.mark, JavaCourse.max_mark
        FROM Person, Courses, JavaCourse
        WHERE Person.firstname = 'Илья' AND Person.lastname = 'Зенков' AND  Courses.student_id = Person.student_id
        AND Courses.student_course_id = JavaCourse.student_course_id
         */

        /*
        Все темы
        SELECT DISTINCT JavaCourse.topic_name, JavaCourse.student_course_id
        FROM Person, Courses, JavaCourse
        WHERE Person.firstname = 'Илья' AND Person.lastname = 'Зенков' AND  Courses.student_id = Person.student_id
        AND Courses.student_course_id = JavaCourse.student_course_id
         */
        /*
        Оценки за конкретную тему
        SELECT JavaCourse.task_name, JavaCourse.mark
        FROM JavaCourse
        WHERE JavaCourse.student_course_id = 8 and JavaCourse.topic_name = "4. ООП. Основы"
         */
        return strB.toString();
    }

    private static int getStudentScore(String name, String course) throws SQLException {
        var parsedName = getParsedName(name);
        var firstName = parsedName[0];
        var lastName = parsedName[1];
        var command = String.format(
                "SELECT SUM(mark) as score " +
                        "FROM Person, Courses, JavaCourse " +
                        "WHERE Person.firstname = \"%s\" AND Person.lastname = \"%s\" AND  Courses.student_id = Person.student_id " +
                        "AND Courses.student_course_id = JavaCourse.student_course_id ",
                firstName, lastName);
        ResultSet resSet = statmt.executeQuery(command);
        return resSet.getInt("score");

    }

    private static String spaceCreator(int count) {
        return " ".repeat(count);
    }

    //Статистика для графиков

    public static ArrayList<String> getCities() throws SQLException {
        var command = "SELECT home_town FROM Person WHERE home_town != 'none'";
        var resSet = statmt.executeQuery(command);
        if (resSet.isClosed()) {
            return null;
        }
        var cities = new ArrayList<String>();
        while (resSet.next()) {
            cities.add(resSet.getString("home_town"));
        }

        return cities;
    }

    public static ArrayList<String> getAge() throws SQLException {
        //\d{2}\.\d{2}\.\d{4}
        var command = "SELECT birthday FROM Person";
        var resSet = statmt.executeQuery(command);
        if (resSet.isClosed()) {
            return null;
        }
        var ages = new ArrayList<String>();
        while (resSet.next()) {
            ages.add(resSet.getString("birthday"));
        }
        var a = ages.stream()
                .filter(s -> Pattern.matches("\\d{1,2}\\.\\d{1,2}\\.\\d{4}", s))
                .collect(Collectors.toList());
        return (ArrayList<String>) a;

    }

    public static ArrayList<Tuple<String, Integer>> getTasksAndMarksOfTopic(String topicName) throws SQLException {

        var command = String.format("SELECT task_name, mark FROM JavaCourse WHERE topic_name = \"%s\" ", topicName);
        var resSet = statmt.executeQuery(command);
        if (resSet.isClosed()) {
            return null;
        }
        var tuples = new ArrayList<Tuple<String, Integer>>();
        while (resSet.next()) {
            var taskName = resSet.getString("task_name");
            var mark = resSet.getInt("mark");
            tuples.add(new Tuple<>(taskName, mark));
        }

        return tuples;
    }

    public static ArrayList<String> getGenders() throws SQLException {
        var command = "SELECT gender FROM 'Person' ";
        var resSet = statmt.executeQuery(command);
        if (resSet.isClosed()) {
            return null;
        }
        var genders = new ArrayList<String>();
        while (resSet.next()) {
            genders.add(resSet.getString("gender"));
        }

        return genders;
    }

    public static ArrayList<Tuple<String, Tuple<String, Integer>>> getCitiesAndTasksMarks(String topicName) throws SQLException {
        var command = String.format(
                "SELECT Person.home_town, JavaCourse.task_name, JavaCourse.mark  " +
                        "FROM Courses, Person, JavaCourse " +
                        "WHERE course_name=\"UlearnJava\" " +
                        "AND Person.student_id = Courses.student_id " +
                        "AND Person.home_town != \"none\" " +
                        "AND Courses.student_course_id = JavaCourse.student_course_id " +
                        "AND JavaCourse.topic_name =\"%s\" ", topicName);
        var resSet = statmt.executeQuery(command);
        if (resSet.isClosed()) {
            return null;
        }
        var tuples = new ArrayList<Tuple<String, Tuple<String, Integer>>>();
        while (resSet.next()) {
            var homeTown = resSet.getString("home_town");
            var taskName = resSet.getString("task_name");
            var mark = resSet.getInt("mark");
            tuples.add(new Tuple<>(homeTown, new Tuple<>(taskName, mark)));
        }
        return tuples;
    }


}


