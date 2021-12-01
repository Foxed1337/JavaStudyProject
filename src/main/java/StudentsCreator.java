import com.google.gson.JsonArray;

import java.util.ArrayList;

public class StudentsCreator {

    public static Students CreateStudents(ArrayList<String[]> table, JsonArray vkData) {

        var students = new Students();

        for (int i = 3; i < table.size(); i++) {
            //Создаем студента: заполняем поля имени, фамилии и группы
            Student student = getStudentFromTable(table, i);
            addInformationFromVK(student, vkData);

            //Добавляем студенту курс(в данном случае ULearnJava)
            student.addCourse(getPersonalizeCourse(table, i));
            students.students.add(student);
        }
        return students;
    }

    private static Student getStudentFromTable(ArrayList<String[]> table, int row) {
        var group = table.get(row)[1];
        var nameAndLastName = table.get(row)[0].split(" ");
        var firstName = nameAndLastName[1];
        var lastName = nameAndLastName[0];
        return new Student(firstName, lastName, group);
    }

    private static Course getPersonalizeCourse(ArrayList<String[]> table, int row) {

        //получаем шапку курса т.е. список тем и заданий, которые включает в себя данная тема
        var personalizedCourse = UlearnJavaCourseCreator.CreateCurse(table);

        //Заполняем курс студента. Вносим оценки за каждое задание в каждой теме
        for (var topic : personalizedCourse.getAllTopics().values()) {
            for (var task : topic.getAllTask().values()) {
                var mark = Integer.parseInt(table.get(row)[task.getPositionOfColumn()]);
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

            if (student.getFirstName().equals(firstName) && student.getLastName().equals(lastName)) {

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
                break;
            }
        }
    }
}
