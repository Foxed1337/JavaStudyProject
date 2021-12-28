public class Person {
    private String firstname = "none";
    private String lastName = "none";
    private String birthday = "none";
    private int VK_ID = 0;
    private String homeTown = "none";
    private String gender = "none";
    private int personID;


    public Person(String firstName, String lastName) {
        this.firstname = firstName;
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthday() {
        return birthday;
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
