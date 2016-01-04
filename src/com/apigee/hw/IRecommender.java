package com.apigee.hw;

import java.util.Map;
import java.util.Set;

/**
 * Created by adi on 12/30/15.
 */
public interface IRecommender {

    Map<Person, Set<Person>> recommend(Set<Person> input, int max);


}
