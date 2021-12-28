import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Course {
    private String name;
    private String studentGroup;
    private int maxScore;
    private Map<String, Topic> Topics = new LinkedHashMap<>();


    public Course(String name) {
        this.name = name;
    }

    public void setMaxScore(int score) {
        this.maxScore = score;
    }

    public int getMaxScore() {
        return this.maxScore;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }

    public void addTopic(Topic topic) {
        Topics.put(topic.getName(), topic);
    }

    public Topic getTopic(String topicName) {
        return Topics.get(topicName);
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Topic> getAllTopics() {
        return this.Topics;
    }
}
