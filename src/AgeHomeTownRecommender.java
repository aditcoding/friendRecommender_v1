import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by adi on 12/30/15.
 */
public class AgeHomeTownRecommender implements IRecommender {

    private Map<String, Set<Person>> ageInvIdx;
    private Map<String, Set<Person>> hometownInvIdx;

    @Override
    public Map<Person, Set<Person>> recommend(Set<Person> input, int max) {
        Map<Person, Set<Person>> m =new HashMap<>();
        createInvertedIndices(input);
        for (Person p : input) {
            String age = p.age;
            String hometown = p.hometown;
            Set<Person> sameAge = ageInvIdx.get(age); // can use +- age instead of exact
            Set<Person> sameHometown = hometownInvIdx.get(hometown);
            sameAge.retainAll(sameHometown); // Intersection
            m.put(p, sameAge);
        }
        return m;
    }

    private void createInvertedIndices(Set<Person> input) {
        if (ageInvIdx == null) ageInvIdx = new HashMap<>();
        if (hometownInvIdx == null) hometownInvIdx = new HashMap<>();
        for (Person p : input) {
            // Create school inv idx
            String age = p.age;
            if (ageInvIdx.containsKey(age)) {
                Set<Person> persons = ageInvIdx.get(age);
                persons.add(p);
            } else {
                Set<Person> persons = new HashSet<>();
                persons.add(p);
                ageInvIdx.put(age, persons);
            }
            // Create hometown inv idx
            String hometown = p.hometown;
            if (hometownInvIdx.containsKey(hometown)) {
                Set<Person> persons = hometownInvIdx.get(hometown);
                persons.add(p);
            } else {
                Set<Person> persons = new HashSet<>();
                persons.add(p);
                hometownInvIdx.put(hometown, persons);
            }
        }
    }
}
