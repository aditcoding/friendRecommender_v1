package com.apigee.hw;

import java.util.Comparator;
import java.util.Set;

/**
 * Created by adi on 12/30/15.
 */
public class Person implements Cloneable {
    public String id, name, age, hometown, school;
    public double quantifier; //Generic
    private Set<String> friendIds;

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

    @Override
    public String toString() {
        return "[" + id + ", " + name + ", " + age + ", " + hometown + ", " + school + ", " + quantifier + "]";
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

    public Person clone() {
        try {
            return (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class PersonQuantifierComparator implements Comparator<Person> {

        @Override
        public int compare(Person o1, Person o2) {
            int comp = Double.compare(o1.quantifier, o2.quantifier);
            if (comp == 0) return o1.id.compareTo(o2.id);
            return comp;
        }
    }
}
