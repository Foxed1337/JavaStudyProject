import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        DBManager.Conn();
        DBManager.CreateDB();


        while (true) {
            sayHi();
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
                    System.out.println(DBManager.getAllStudents());
                    break;
                case "ABOUT_STUD":
                    System.out.println("Введите имя студента: ");
                    var studName = sc.nextLine();
                    System.out.println(DBManager.getPersonInfo(studName));
                    System.out.println(DBManager.getStudentCourses(studName));
                    break;
                case "PROGRESS":
                    System.out.println("Введите имя студента: ");
                    var name = sc.nextLine();
                    System.out.println("Введите курс студента (UlearnJava)");
                    var course = sc.nextLine();
                    System.out.println(DBManager.getStudentCourseProgress(name,course));
                    break;
                default:
                    System.out.println("Команда введена не верно");
            }
        }
    }

    private static void parseData(String path) throws ClientException, InterruptedException, ApiException, SQLException {
        var parser = new CSV_Parser();
        var table = parser.getParsedCVSFromPath(path, ";");
        var vkParser = new VK_Parser();
        var vkData = vkParser.getJsonMembersOfGroup(Config.IOT_GROUP_ID);
        var sts = StudentsCreator.CreateStudents(table, vkData);
    }

    private static void sayHi() {
        System.out.println("---------------------------\n" +
                "Доступные команды:\n" +
                "STUDENTS - получить список всех студентов\n" +
                "ABOUT_STUD - получить информацию о студенте\n" +
                "EXIT - выйти из программы\n" +
                "ADD_DATA - загрузить данные о студентах\n" +
                "PROGRESS - получить прогресс студента по курсу\n" +
                "---------------------------\n");
    }
}


