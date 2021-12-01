public class Person {
    private String firstName;
    private String lastName;
    private String fullName;
    private String  birthday;
    private int VK_ID;
    private String homeTown;


    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
    }

    public Person(String fullName) {
        this.firstName = "Имя";
        this.lastName = "Фамилия";
        this.fullName = fullName;
    }



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getFullName() {
        return fullName;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public int getVK_ID() {
        return VK_ID;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setVK_ID(int VK_ID) {
        this.VK_ID = VK_ID;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }
}
