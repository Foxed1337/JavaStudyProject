

public class Main {
    public static void main(String[] args) throws Exception {

        var parser = new CSV_Parser();
        var table = parser.getParsedCVSFromPath("src/main/resources/java-rtf (5).csv", ";");
        var vkParser = new VK_Parser();
        var vkData = vkParser.getJsonMembersOfGroup(198188261);
        var sts = StudentsCreator.CreateStudents(table, vkData);
        var st = sts.students.get(59);
        System.out.println(st.toString());
    }
}


