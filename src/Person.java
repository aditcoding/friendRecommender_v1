import java.util.Set;

/**
 * Created by adi on 12/30/15.
 */
public class Person {
    public String id, name, age, hometown, school;
    private Set<String> friendIds;
    private Set<String> recommendedFriends;

    public Person(String id, String name, String age, String hometown, String school) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.hometown = hometown;
        this.school = school;
    }

    public Set<String> getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(Set<String> fIds) {
        friendIds = fIds;
    }

    public Set<String> getRecommendedFriends() {
        return recommendedFriends;
    }

    public void setRecommendedFriends(Set<String> recommendedFriends) {
        this.recommendedFriends = recommendedFriends;
    }

    @Override
    public String toString() {
        return "[" + id + ", " + name + ", " + age + ", " + hometown + ", " + school + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person o = (Person) obj;
            return id.equals(o.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(id);
    }
}
