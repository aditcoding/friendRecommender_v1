package com.apigee.hw;

import com.apigee.hw.recommenders.AgeHomeTownRecommender;
import com.apigee.hw.recommenders.MutualFriendRecommender;
import com.apigee.hw.recommenders.SchoolHomeTownRecommender;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * Created by adi on 1/2/16.
 */
public class Aggregator {

    private static final double MUTUAL_FRIEND_WEIGHT2 = 0.7;
    private static final double SCHOOL_HOMETOWN_WEIGHT = 0.6;
    private static final double MUTUAL_FRIEND_WEIGHT1 = 0.4;
    private static final double AGE_HOMETOWN_WEIGHT = 0.3;


    public Map<Person, Set<Person>> aggregate(Map<IRecommender, Map<Person, Set<Person>>> parts, int maxRecos) {
        Map<Person, Map<Person, Double>> weightsMap = new HashMap<>();
        for (Map.Entry<IRecommender, Map<Person, Set<Person>>> e : parts.entrySet()) {
            IRecommender ir = e.getKey();
            Map<Person, Set<Person>> val = e.getValue();
            for (Map.Entry<Person, Set<Person>> e1 : val.entrySet()) {
                Person p = e1.getKey();
                Set<Person> recos = e1.getValue();
                Map<Person, Double> weights;
                if (weightsMap.containsKey(p)) weights = weightsMap.get(p);
                else weights = new HashMap<>();
                for (Person p1 : recos) {
                    double weight = 0;
                    if (weights.containsKey(p1)) weight = weights.get(p1);
                    if (ir instanceof MutualFriendRecommender) {
                        double ratio = p1.quantifier;
                        if (ratio >= 0.1) weight += MUTUAL_FRIEND_WEIGHT2;
                        else if (ratio > 0 && ratio < 0.1) weight += MUTUAL_FRIEND_WEIGHT1;
                    } else if (ir instanceof SchoolHomeTownRecommender) weight += SCHOOL_HOMETOWN_WEIGHT;
                    else if (ir instanceof AgeHomeTownRecommender) weight += AGE_HOMETOWN_WEIGHT;
                    Person tmp = p1.clone(); // shallow clone
                    tmp.quantifier = weight;
                    weights.put(tmp, weight);
                }
                weightsMap.put(p, weights);
            }
        }
        return getTopRecos(weightsMap, maxRecos);
    }

    private Map<Person, Set<Person>> getTopRecos(Map<Person, Map<Person, Double>> weightsMap, int maxRecos) {
        Map<Person, Set<Person>> ret = new HashMap<>(maxRecos);
        for (Map.Entry<Person, Map<Person, Double>> e : weightsMap.entrySet()) {
            Person p = e.getKey();
            Map<Person, Double> sorted = sortByValue(e.getValue());
            SortedSet<Person> topRecos = new TreeSet<>(Collections.reverseOrder(new Person.PersonQuantifierComparator()));
            int i = 0;
            Iterator<Map.Entry<Person, Double>> iter = sorted.entrySet().iterator();
            while (iter.hasNext() && i++ < 5) {
                Map.Entry<Person, Double> next = iter.next();
                Person tmp = next.getKey();
                tmp.quantifier = next.getValue();
                topRecos.add(tmp);
            }
            ret.put(p, topRecos);
        }
        return ret;
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();
        st.sorted(Comparator.comparing(e -> e.getValue(), Collections.reverseOrder()))
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

}