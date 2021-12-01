import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CVS_Parser {

    private String file;

    public ArrayList<String[]> getParsedCVSFromPath(String filePath, String separator) {
        /*
        Парсит CSV в лист строк таблицы
         */
        file = filePath;
        String line = "";
        BufferedReader reader = null;
        ArrayList<String[]> results = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                results.add(line.split(separator));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

}
