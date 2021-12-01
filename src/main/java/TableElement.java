public class TableElement {
    /*
    Данный класс хранит положение элемента таблици(CVS Файла)
     */
    private final int position_of_column;
    private final int position_of_row;

    public TableElement(int columnPos, int rowPos) {
        position_of_column = columnPos;
        position_of_row = rowPos;
    }

    public int getPositionOfColumn() {
        return position_of_column;
    }

    public int getPositionOfRow() {
        return position_of_row;
    }
}
