import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by adi on 12/30/15.
 */
public class SchoolHomeTownRecommender implements IRecommender{

    private Map<String, Set<Person>> schoolInvIdx;
    private Map<String, Set<Person>> hometownInvIdx;

    @Override
    public Map<Person, Set<Person>> recommend(Set<Person> input, int max) {
        Map<Person, Set<Person>> m =new HashMap<>();
        createInvertedIndices(input);
        for (Person p : input) {
            String school = p.school;
            String hometown = p.hometown;
            Set<Person> sameSchool = schoolInvIdx.get(school);
            Set<Person> sameHometown = hometownInvIdx.get(hometown);
            sameSchool.retainAll(sameHometown); // Intersection
            m.put(p, sameSchool);
        }
        return m;
    }

    private void createInvertedIndices(Set<Person> input) {
        if (schoolInvIdx == null) schoolInvIdx = new HashMap<>();
        if (hometownInvIdx == null) hometownInvIdx = new HashMap<>();
        for (Person p : input) {
            // Create school inv idx
            String school = p.school;
            if (schoolInvIdx.containsKey(school)) {
                Set<Person> persons = schoolInvIdx.get(school);
                persons.add(p);
            } else {
                Set<Person> persons = new HashSet<>();
                persons.add(p);
                schoolInvIdx.put(school, persons);
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
