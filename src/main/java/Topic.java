import java.util.HashMap;
import java.util.LinkedHashMap;
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
    private Map<String, Task> tasks = new LinkedHashMap<>();

    public Topic(String name, int columnPos, int rowPos) {
        super(columnPos, rowPos);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, Task> getAllTask() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.put(task.getName(), task);
    }

    //Костыль
    private int count = 1;

    public void addTask(String taskName, int columnPos, int rowPos) {
        //TODO : Сделать костыль элегантнее
        var key = "ДЗ: Контрольный вопрос";
        if (taskName.trim().equals(key)) {
            taskName = String.format("%s %s", taskName, count);
            tasks.put(taskName, new Task(taskName, columnPos, rowPos));
            count++;
        } else {
            tasks.put(taskName, new Task(taskName, columnPos, rowPos));
        }

    }


    public Task getTask(String taskName) {
        return tasks.get(taskName);
    }

}
