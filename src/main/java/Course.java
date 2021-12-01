import java.util.HashMap;
import java.util.Map;

public class Course {
    private String name;
    private Map<String, Topic> Topics = new HashMap<>();

    public Course(String name) {
        this.name = name;
    }

    public void addTopic(Topic topic) {
        Topics.put(topic.getName(), topic);
    }

    public Topic getTopic(String topicName) {
        return Topics.get(topicName);
    }

    public String getName() {
        return name;
    }

    public Map<String, Topic> getAllTopics() {
        return Topics;
    }
}
