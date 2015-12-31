import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by adi on 12/30/15.
 */
public class MutualFriendRecommender implements IRecommender {

    @Override
    public Map<Person, Set<Person>> recommend(Set<Person> input, int max) {
        //Putting all values in map
        Map<String, Person> m = new HashMap<>();
        Map<Person, Set<Person>> ret = new HashMap<>();
        for (Person p : input) {
            m.put(p.id, p);
        }
        for (Person p : input) {
            Set<Person> secondDegree = new HashSet<>();
            for (String s : p.getFriendIds()) {
                // second degree friends of person p
                secondDegree.add(m.get(s));
            }
            ret.put(p, secondDegree);
        }
        return ret;
    }
}
