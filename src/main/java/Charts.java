import java.time.LocalDate;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Charts {

    public static void drawCitiesStatistics() throws SQLException {
        var cities = DBManager.getCities();
        if (cities == null) {
            System.out.println("Не удалось получить данные");
            return;
        }
        var citiesStat = new HashMap<String, Integer>();
        for (var city : cities) {
            if (citiesStat.containsKey(city)) {
                citiesStat.put(city, citiesStat.get(city) + 1);
            } else {
                citiesStat.put(city, 1);
            }
        }
        BarCitiesStatictic.draw("ГОРОДА", citiesStat);
        System.out.println();
    }

    public static void drawAgeStatistics() throws SQLException {
        var birthdays = DBManager.getAge();
        if (birthdays == null) {
            System.out.println("Не удалось получить данные");
            return;
        }
        var agesStat = new HashMap<Integer, Integer>();
        var ages = birthdays.stream()
                .map(x -> LocalDate.now().getYear() - Integer.parseInt(x.split("\\.")[2]))
                .collect(Collectors.toList());

        for (var age : ages) {
            if (agesStat.containsKey(age)) {
                agesStat.put(age, agesStat.get(age) + 1);
            } else {
                agesStat.put(age, 1);
            }
        }
        PieAgesStatictic.draw("Статистика по возрастам", agesStat);
        System.out.println();
    }

    public static void drawTopicStatistic(String topicName) throws SQLException {

        var topicsAndMarks = DBManager.getTasksAndMarksOfTopic(topicName);
        if (topicsAndMarks == null) {
            System.out.println("Не удалось получить данные");
            return;
        }
        var topicStat = new HashMap<String, Integer>();

        for (var i : topicsAndMarks) {
            if (i.y != 0) {
                if (topicStat.containsKey(i.x)) {
                    topicStat.put(i.x, topicStat.get(i.x) + 1);
                } else {
                    topicStat.put(i.x, i.y);
                }
            }
        }
        BarTopicStatistic.draw(topicName, topicStat);
        System.out.println();
    }

    public static void drawGenderStatistic() throws SQLException {
        var genders = DBManager.getGenders();
        if (genders == null) {
            System.out.println("Не удалось получить данные");
            return;
        }
        var gendersStat = new HashMap<String, Integer>();

        for (var gender : genders) {
            if (gendersStat.containsKey(gender)) {
                gendersStat.put(gender, gendersStat.get(gender) + 1);
            } else {
                gendersStat.put(gender, 1);
            }
        }
        PieGendersStatictic.draw("Половая принадлежность студентов курса", gendersStat);
        System.out.println();
    }

//    public static  void drawCitiesAndTaskMarksStatistic(String topicName) throws SQLException {
//
//        var townsAndTasks = DBManager.getCitiesAndTasksMarks(topicName);
//        System.out.println();
//        //ScatterCitiesAndTasksMarksStatictic.draw();
//    }

}
