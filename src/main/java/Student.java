import java.util.HashMap;
import java.util.Map;

public class Student extends Person {
    private Map<String, Course> courses = new HashMap<>();

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Map<String, Course> getCourses(){
        return courses;
    }


    public void addCourse(Course course) {
        courses.put(course.getName(), course);
    }

    @Override
    public String toString() {
        //TODO Сделать нормальный toString()
        return "";
    }
}
