public class Task extends TableElement {
    /*
    Класс задания. Пример задания на ULearn(Взято из ведомости)
    name: ДЗ: 1.1 Контрольные вопросы
    mark: 2
     */
    private String name;
    private int mark;
    private int MAX_MARK;


    public Task(String name, int columnPos, int rowPos) {
        super(columnPos, rowPos);
        this.name = name;

    }

    public void setMark(int mark) {
        this.mark = mark;
    }


    public String getName() {
        return name;
    }

    public int getMark() {
        return mark;
    }

    public int getMAX_MARK() {
        return MAX_MARK;
    }
}
