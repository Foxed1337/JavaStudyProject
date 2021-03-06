import com.google.gson.JsonArray;

import java.sql.SQLException;
import java.util.ArrayList;

public class StudentsCreator {

    public static void CreateStudents(ArrayList<String[]> table, JsonArray vkData) throws SQLException {


        var lastPersonID = getLastPersonIDFromDB();

        for (int i = 3; i < table.size(); i++) {
            //Создаем студента: заполняем поля имени, фамилии и группы
            Student student = getStudentFromTable(table, i);

            //Задаем уникальный ID Студента (точнее Person)
            student.setPersonID(i - 2 + lastPersonID);

            //Добавляем информацию из ВК
            addInformationFromVK(student, vkData);

            //Добавляем студенту курс(в данном случае ULearnJava)
            student.addCourse(getPersonalizeJavaCourse(table, i));

            //Скидываем студента в общуюю кучу студентов


            //Записываем студента в БД
            DBManager.WriteStudentToDB(student);
        }

    }

    private static int getLastPersonIDFromDB() {
        if (!DBManager.isConnected()) {
            try {
                DBManager.Conn();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return 0;
            }
        }
        try {
            return DBManager.getMaxPersonID();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    private static Student getStudentFromTable(ArrayList<String[]> table, int row) {
        var nameAndLastName = table.get(row)[0].split(" +");
        var firstName = nameAndLastName[1];
        var lastName = nameAndLastName[0];
        return new Student(firstName, lastName);
    }

    private static Course getPersonalizeJavaCourse(ArrayList<String[]> table, int row) {

        //получаем шапку курса т.е. список тем и заданий, которые включает в себя данная тема
        var personalizedCourse = UlearnJavaCourseCreator.CreateCurse(table);
        //Указываем группу, в которой обучаетмся студент на данном курсе
        personalizedCourse.setStudentGroup(table.get(row)[1]);
        //Указываем максимальное количество баллов, которое можно набрать за курс
        personalizedCourse.setMaxScore(Integer.parseInt(table.get(2)[2]));
        //Заполняем курс студента. Вносим оценки за каждое задание в каждой теме
        for (var topic : personalizedCourse.getAllTopics().values()) {
            for (var task : topic.getAllTask().values()) {
                var mark = Integer.parseInt(table.get(row)[task.getPositionOfColumn()]);
                var maxMark = Integer.parseInt(table.get(2)[task.getPositionOfColumn()]);
                task.setMAX_MARK(maxMark);
                task.setMark(mark);
            }
        }
        return personalizedCourse;
    }

    private static void addInformationFromVK(Student student, JsonArray vkData) {
        for (int i = 0; i < vkData.size(); i++) {
            var firstName = vkData
                    .get(i)
                    .getAsJsonObject()
                    .get("first_name")
                    .getAsString();

            var lastName = vkData
                    .get(i)
                    .getAsJsonObject()
                    .get("last_name")
                    .getAsString();

            if (student.getFirstname().equals(firstName) && student.getLastName().equals(lastName)) {

                student.setVK_ID(vkData
                        .get(i)
                        .getAsJsonObject()
                        .get("id")
                        .getAsInt());

                var jsonBirthday = vkData
                        .get(i)
                        .getAsJsonObject()
                        .get("bdate");

                if (jsonBirthday != null) {
                    student.setBirthday(jsonBirthday.getAsString());
                }

                var jsonCity = vkData
                        .get(i)
                        .getAsJsonObject()
                        .get("city");

                if (jsonCity != null) {
                    var jsonCityTitle = jsonCity
                            .getAsJsonObject()
                            .get("title");

                    if (jsonCityTitle != null) {
                        student.setHomeTown(jsonCityTitle.getAsString());
                    }
                }

                var jsonGender = vkData
                        .get(i)
                        .getAsJsonObject()
                        .get("sex");

                if (jsonBirthday != null) {
                    student.setGender(jsonGender.getAsString());
                }
                break;
            }
        }
    }
}
