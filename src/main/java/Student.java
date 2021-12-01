import java.util.HashMap;
import java.util.Map;

public class Student extends Person {
    private String group;
    private Map<String, Course> courses = new HashMap<>();

    public Student(String firstName, String lastName, String group) {
        super(firstName, lastName);
        this.group = group;
    }

    public Student(String fullName, String group) {
        super(fullName);
        this.group = group;
    }

    public void addCourse(Course course) {
        courses.put(course.getName(), course);
    }

    @Override
    public String toString() {
        return "Student{" +
                "group='" + group + '\'' +
                ", courses=" + courses +
                "firstName='" + super.getFirstName() + '\'' +
                ", lastName='" + super.getLastName() + '\'' +
                ", birthday='" + super.getBirthday() + '\'' +
                ", VK_ID=" + super.getVK_ID() +
                ", homeTown='" + super.getHomeTown() + '\'' +
                '}';
    }
}
