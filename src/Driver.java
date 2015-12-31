import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by adi on 12/30/15.
 */
public class Driver {

    public static final int MAX_RECOMMENDATIONS = 5;

    public static void main(String... args) {
        String filePath = "/home/adi/Projects/FriendRecommender/input.txt";
        if (args != null && args.length == 1) {
            filePath = args[0];
        }
        Set<Person> persons = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String readLine;
            while ((readLine = reader.readLine()) != null) {
                if (readLine.startsWith("#") || readLine.trim().isEmpty()) continue;
                //System.out.println(readLine);
                String[] meta = readLine.split("\\[");
                //System.out.println(meta[0]);
                String[] arr = meta[0].split(",");
                String id = arr[0];
                String name = arr[1];
                String age = arr[2];
                String hometown = arr[3];
                String school = arr[4];
                //System.out.print(id+name+age+hometown+school);
                String friendIds = meta[1];
                Set<String> fIds = parseFriendIds(friendIds);
                Person person = new Person(id, name, age, hometown, school);
                person.setFriendIds(fIds);
                persons.add(person);
            }
            invokeRecommenders(persons);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void invokeRecommenders(Set<Person> persons) {
        AgeHomeTownRecommender ageHomeTownRecommender = new AgeHomeTownRecommender();
        Map<Person, Set<Person>> recos1 = ageHomeTownRecommender.recommend(persons, MAX_RECOMMENDATIONS);

        SchoolHomeTownRecommender schoolHomeTownRecommender =  new SchoolHomeTownRecommender();
        Map<Person, Set<Person>> recos2 = schoolHomeTownRecommender.recommend(persons, MAX_RECOMMENDATIONS);

        MutualFriendRecommender mutualFriendRecommender = new MutualFriendRecommender();
        Map<Person, Set<Person>> recos3 = mutualFriendRecommender.recommend(persons, MAX_RECOMMENDATIONS);

        System.out.println("Age_HomeTown: ");
        for (Map.Entry<Person, Set<Person>> e : recos1.entrySet()) {
            Person person = e.getKey();
            Set<Person> recos = e.getValue();
            System.out.println("Recommendations for " + person.id);
            for (Person p : recos) System.out.println(p + ", ");
        }
        System.out.println("School_HomeTown: ");
        for (Map.Entry<Person, Set<Person>> e : recos2.entrySet()) {
            Person person = e.getKey();
            Set<Person> recos = e.getValue();
            System.out.println("Recommendations for " + person.id);
            for (Person p : recos) System.out.println(p + ", ");
        }

        System.out.println("MutualFriends: ");
        for (Map.Entry<Person, Set<Person>> e : recos3.entrySet()) {
            Person person = e.getKey();
            Set<Person> recos = e.getValue();
            System.out.println("Recommendations for " + person.id);
            for (Person p : recos) System.out.println(p + ", ");
        }
    }

    private static Set<String> parseFriendIds(String friendIds) {
        friendIds = friendIds.substring(0, friendIds.length());
        String[] arr = friendIds.split(",");
        Set<String> set = new HashSet<>(arr.length);
        for (String s : arr) set.add(s);
        return set;
    }
}
