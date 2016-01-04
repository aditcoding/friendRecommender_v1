package com.apigee.hw;

import com.apigee.hw.recommenders.AgeHomeTownRecommender;
import com.apigee.hw.recommenders.MutualFriendRecommender;
import com.apigee.hw.recommenders.SchoolHomeTownRecommender;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
                String id = arr[0].trim();
                String name = arr[1].trim();
                String age = arr[2].trim();
                String hometown = arr[3].trim();
                String school = arr[4].trim();
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
        IRecommender ageHomeTownRecommender = new AgeHomeTownRecommender();
        Map<Person, Set<Person>> recos1 = ageHomeTownRecommender.recommend(persons, MAX_RECOMMENDATIONS);

        IRecommender schoolHomeTownRecommender =  new SchoolHomeTownRecommender();
        Map<Person, Set<Person>> recos2 = schoolHomeTownRecommender.recommend(persons, MAX_RECOMMENDATIONS);

        IRecommender mutualFriendRecommender = new MutualFriendRecommender();
        Map<Person, Set<Person>> recos3 = mutualFriendRecommender.recommend(persons, MAX_RECOMMENDATIONS);

        Map<IRecommender, Map<Person, Set<Person>>> combinedRecos = new HashMap<>();
        combinedRecos.put(ageHomeTownRecommender, recos1);
        combinedRecos.put(schoolHomeTownRecommender, recos2);
        combinedRecos.put(mutualFriendRecommender, recos3);

        Aggregator aggregator = new Aggregator();
        Map<Person, Set<Person>> result = aggregator.aggregate(combinedRecos, MAX_RECOMMENDATIONS);

        System.out.println("Result: ");
        for (Map.Entry<Person, Set<Person>> e : result.entrySet()) {
            Person person = e.getKey();
            Set<Person> recos = e.getValue();
            System.out.println("Recommendations for " + person.id);
            for (Person p : recos) System.out.println(p + ", ");
        }

        /*System.out.println("Age_HomeTown: ");
        for (Map.Entry<com.apigee.hw.Person, Set<com.apigee.hw.Person>> e : recos1.entrySet()) {
            com.apigee.hw.Person person = e.getKey();
            Set<com.apigee.hw.Person> recos = e.getValue();
            System.out.println("Recommendations for " + person.id);
            for (com.apigee.hw.Person p : recos) System.out.println(p + ", ");
        }
        System.out.println("School_HomeTown: ");
        for (Map.Entry<com.apigee.hw.Person, Set<com.apigee.hw.Person>> e : recos2.entrySet()) {
            com.apigee.hw.Person person = e.getKey();
            Set<com.apigee.hw.Person> recos = e.getValue();
            System.out.println("Recommendations for " + person.id);
            for (com.apigee.hw.Person p : recos) System.out.println(p + ", ");
        }

        System.out.println("MutualFriends: ");
        for (Map.Entry<com.apigee.hw.Person, Set<com.apigee.hw.Person>> e : recos3.entrySet()) {
            com.apigee.hw.Person person = e.getKey();
            Set<com.apigee.hw.Person> recos = e.getValue();
            System.out.println("Recommendations for " + person.id);
            for (com.apigee.hw.Person p : recos) System.out.println(p + ", ");
        }*/
    }

    private static Set<String> parseFriendIds(String friendIds) {
        friendIds = friendIds.substring(0, friendIds.length()-1);
        String[] arr = friendIds.split(",");
        Set<String> set = new HashSet<>(arr.length);
        for (String s : arr) set.add(s.trim());
        return set;
    }
}
