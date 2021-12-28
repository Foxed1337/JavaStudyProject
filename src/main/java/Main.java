import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        DBManager.Conn();
        DBManager.CreateDB();
        //Charts.drawCitiesStatistics();
        //Charts.drawAgeStatistics();
        //Charts.drawTopicStatistic("2. Базовый синтаксис. Типы");
        //Charts.drawGenderStatistic();
        while (true) {
            sayHi();
            String studentName;
            switch (sc.nextLine()) {
                case "EXIT":
                    DBManager.CloseDB();
                    System.exit(0);
                    break;
                case "ADD_DATA":
                    System.out.println("Хотите использовать свой CSV файл? [Y/N]");
                    if (sc.nextLine().equals("Y")) {
                        System.out.println("Введите путь до CSV файла: ");
                        parseData(sc.nextLine());
                        break;
                    } else {
                        parseData(Config.PATH_TO_CSV);
                    }
                    break;
                case "STUDENTS":
                    var students = DBManager.getAllStudents();
                    System.out.println(Objects.requireNonNullElse(students, "Не удалось получть список студентов"));

                    break;
                case "ABOUT_STUD":
                    System.out.println("Введите имя студента: ");
                    studentName = sc.nextLine();
                    var personInfo = DBManager.getPersonInfo(studentName);
                    if (personInfo != null) {
                        System.out.println(personInfo);
                    } else {
                        System.out.printf("Не удалось найти студента: %s%n", studentName);
                    }

                    System.out.println("Хотите узнать о курсе студента? [Y/N]");
                    if (sc.nextLine().equals("Y")) {
                        var courses = DBManager.getStudentCourses(studentName);
                        System.out.println(Objects.requireNonNullElse(courses, "Нет информации об курсах данного студента"));
                        var progress = DBManager.getStudentCourseProgress(studentName, "");
                        System.out.println(Objects.requireNonNullElse(progress, "Не удалось получить прогресс по курсу"));
                    }
                    break;
                case "PROGRESS":
                    System.out.println("Введите имя студента: ");
                    studentName = sc.nextLine();
                    System.out.println(DBManager.getStudentCourses(studentName));
                    System.out.println(DBManager.getStudentCourseProgress(studentName, ""));
                    break;
                case "CitiesStatistics":
                    Charts.drawCitiesStatistics();
                    break;
                case "AgeStatistics":
                    Charts.drawAgeStatistics();
                    break;
                case "TopicStatistic":
                    System.out.println("Введите название темы: ");
                    Charts.drawTopicStatistic(sc.nextLine());
                    break;
                case "GenderStatistic":
                    Charts.drawGenderStatistic();
                default:
                    System.out.println("Команда введена не верно");
            }
        }

    }

    private static void parseData(String path) throws ClientException, InterruptedException, ApiException, SQLException {

        var table = CSV_Parser.getParsedCVSFromPath(path, ";");
        var vkParser = new VK_Parser();
        var vkData = vkParser.getJsonMembersOfGroup(Config.IRIT_GROUP_ID);
        StudentsCreator.CreateStudents(table, vkData);
        System.out.println();
    }

    private static void sayHi() {
        System.out.println("---------------------------\n" +
                "Доступные команды:\n" +
                "STUDENTS - получить список всех студентов\n" +
                "ABOUT_STUD - получить информацию о студенте\n" +
                "EXIT - выйти из программы\n" +
                "ADD_DATA - загрузить данные о студентах\n" +
                "PROGRESS - получить прогресс студента по курсу\n" +
                "CitiesStatistics - статистику городов студентов записанных на курс\n" +
                "AgeStatistics - получить статистику возрастов студентов записанных на кур\n" +
                "TopicStatistic - получить количество решенных задач в теме\n" +
                "GenderStatistic - получить половую структуру по курсу\n" +
                "---------------------------\n");
    }
}


