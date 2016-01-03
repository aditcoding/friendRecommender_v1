import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by adi on 12/30/15.
 */
public class MutualFriendRecommender implements IRecommender {

    class PersonQuantifierComparator implements Comparator<Person> {

        @Override
        public int compare(Person o1, Person o2) {
            return Double.compare(o1.quantifier, o2.quantifier);
        }
    }

    public static final int MAX_RECOMMENDATIONS = 5;

    @Override
    public Map<Person, Set<Person>> recommend(Set<Person> input, int max) {
        Map<Person, Set<Person>> ret = new HashMap<>();
        //Putting all values in map
        Map<String, Person> m = new HashMap<>();
        for (Person p : input) m.put(p.id, p);
        for (Person p : input) {
            PriorityQueue<Person> pq = new PriorityQueue<>(MAX_RECOMMENDATIONS, new PersonQuantifierComparator());
            Set<String> secondDegree = p.getFriendIds();
            Set<String> secondDegreeCopy = new HashSet<>();
            for (String s : secondDegree) {
                secondDegreeCopy.addAll(secondDegree);
                int totalFriends = secondDegree.size() + m.get(s).getFriendIds().size();
                secondDegreeCopy.retainAll(m.get(s).getFriendIds());
                int numMutual = secondDegreeCopy.size();
                double ratio = (double) numMutual / (totalFriends - numMutual);
                Person tmp = m.get(s);
                tmp.quantifier = ratio;
                if (pq.size() < 5) pq.add(tmp);
                else if (pq.size() == 5 && pq.peek().quantifier < ratio) {
                    pq.poll();
                    pq.add(tmp);
                }
            }
            Set<Person> topFive = new HashSet<>(5);
            outer:
            while (!pq.isEmpty()) {
                Person p1 = pq.poll();
                Iterator<String> iter = p1.getFriendIds().iterator();
                while (iter.hasNext()) {
                    String next = iter.next();
                    if (next.equals(p.id) || p.getFriendIds().contains(next)) continue;
                    topFive.add(m.get(next));
                    if (topFive.size() == 5) break outer;
                }
            }
            ret.put(p, topFive);
        }
        return ret;
    }
}
