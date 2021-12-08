

public class Main {
    public static void main(String[] args) throws Exception {



        DBManager.Conn();
        DBManager.CreateDB();

        var parser = new CSV_Parser();
        var table = parser.getParsedCVSFromPath("src/main/resources/java-rtf (5).csv", ";");
        var vkParser = new VK_Parser();
        var vkData = vkParser.getJsonMembersOfGroup(198188261);
        var sts = StudentsCreator.CreateStudents(table, vkData);


        for (var st : sts.students) {
            DBManager.WriteStudentToDB(st);
        }
        DBManager.ReadDB();

        DBManager.CloseDB();
    }
}


