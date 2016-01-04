package com.apigee.hw.recommenders;

import com.apigee.hw.IRecommender;
import com.apigee.hw.Person;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by adi on 12/30/15.
 */
public class MutualFriendRecommender implements IRecommender {

    public static final int MAX_RECOMMENDATIONS = 5;

    @Override
    public Map<Person, Set<Person>> recommend(Set<Person> input, int max) {
        Map<Person, Set<Person>> ret = new HashMap<>();
        //Putting all values in map
        Map<String, Person> m = new HashMap<>();
        for (Person p : input) m.put(p.id, p);
        PriorityQueue<Person> pq = new PriorityQueue<>(MAX_RECOMMENDATIONS, new Person.PersonQuantifierComparator());
        Set<String> secondDegree = new HashSet<>();
        for (Person p : input) {
            pq.clear();
            secondDegree.clear();
            Set<String> fIds = p.getFriendIds();
            for (String s : fIds) {
                secondDegree.addAll(fIds); // shallow copy
                int totalFriends = fIds.size() + m.get(s).getFriendIds().size() - 2; // -2 for excluding each other
                secondDegree.retainAll(m.get(s).getFriendIds());
                int numMutual = secondDegree.size();
                double ratio;
                if (totalFriends == numMutual) ratio = 1;
                else ratio = (double) numMutual / (totalFriends - numMutual);
                Person tmp = m.get(s);
                tmp.quantifier = ratio;
                if (pq.size() < 5) pq.add(tmp);
                else if (pq.size() == 5 && pq.peek().quantifier < ratio) {
                    pq.poll();
                    pq.add(tmp);
                }
            }
            SortedSet<Person> topFive = new TreeSet<>(Collections.reverseOrder(new Person.PersonQuantifierComparator()));
            outer:
            while (!pq.isEmpty()) {
                Person p1 = pq.poll();
                Iterator<String> iter = p1.getFriendIds().iterator();
                while (iter.hasNext()) {
                    String next = iter.next();
                    if (next.equals(p.id) || fIds.contains(next)) continue;
                    Person tmp = m.get(next).clone(); // shallow clone
                    tmp.quantifier = p1.quantifier;
                    topFive.add(tmp);
                    if (topFive.size() == 5) break outer;
                }
            }
            ret.put(p, topFive);
        }
        return ret;
    }
}
