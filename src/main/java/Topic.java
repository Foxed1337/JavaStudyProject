import java.util.HashMap;
import java.util.Map;
/*
Класс темы. Пример темы из курса Ulearn:

    name: 1. Введение в Java
    topicTasks:
               name: ДЗ: 1.1 Контрольные вопросы
               mark: 2

               name: ДЗ: 1.2.2 Контрольные вопросы
               mark: 2

 */


public class Topic extends TableElement {
    private String name;
    private Map<String, Task> topicTasks = new HashMap<>();

    public Topic(String name, int columnPos, int rowPos) {
        super(columnPos, rowPos);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, Task> getAllTask() {
        return topicTasks;
    }


    public void addTask(Task task) {
        topicTasks.put(task.getName(), task);
    }

    public void addTask(String taskName, int columnPos, int rowPos) {
        topicTasks.put(taskName, new Task(taskName, columnPos, rowPos));
    }


    public Task getTask(String taskName) {
        return topicTasks.get(taskName);
    }

}