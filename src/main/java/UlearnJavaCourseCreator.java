import java.util.ArrayList;


/*
Данный класс создает курс(шапку кураса т.е. иерархию тем и задач, которые включает в себя тема)
 */
public class UlearnJavaCourseCreator {
    public static Course CreateCurse(ArrayList<String[]> table) {
        var resultCourse = new Course("UlearnJava");

        String lastTopicName = "";

        for (int i = 3; i < table.get(1).length; i++) {
            if (i < table.get(0).length) {
                if (table.get(0)[i] != null && !table.get(0)[i].isEmpty()) {
                    lastTopicName = table.get(0)[i];
                    resultCourse.addTopic(new Topic(lastTopicName, i, 0));
                    continue;
                }
            }
            resultCourse.getTopic(lastTopicName).addTask(table.get(1)[i], i, 1);
        }
        return resultCourse;
    }
}

