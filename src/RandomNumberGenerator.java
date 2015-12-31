import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by adi on 12/30/15.
 */
public class RandomNumberGenerator {

    public static void main(String... args) {
        String names = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWX";
        String[] towns = {"SanFran", "SanJose", "Madison", "NewYork", "Seattle", "Orleans", "Dallas", "LosAngeles", "SanDiego",
                            "Miami", "Cleveland", "Kansas", "Durham", "Charlotte", "Raleigh", "Austin", "Berkeley"};
        String[] schools = {"SanFranHigh", "SanJoseHigh", "MadisonHigh", "NewYorkHigh", "SeattleHigh", "OrleansHigh", "DallasHigh", "LosAngelesHigh",
                              "SanDiegoHigh", "MiamiHigh", "ClevelandHigh", "KansasHigh", "DurhamHigh", "CharlotteHigh", "RaleighHigh", "AustinHigh",
                                "BerkeleyHigh"};
        int maxFriends = 7;
        Map<Integer, Set<Integer>> m = new HashMap<>(50);
        for (int j = 1; j <= 50; j++) {
            int numFriends = ThreadLocalRandom.current().nextInt(1, maxFriends + 1);
            Set<Integer> friendIds = new HashSet<>(numFriends);
            for (int i = 0; i < numFriends; i++) {
                int nextId = ThreadLocalRandom.current().nextInt(1, 51);
                if (nextId != j)
                    friendIds.add(nextId);
            }
            m.put(j, friendIds);
        }
        // Mututalize
        for (Map.Entry<Integer, Set<Integer>> e : m.entrySet()) {
            int key = e.getKey();
            Set<Integer> friendIds = e.getValue();
            for (int i : friendIds) {
                m.get(i).add(key);
            }
        }
        int j = 0;
        for (Map.Entry<Integer, Set<Integer>> e : m.entrySet()) {
            int randAge = ThreadLocalRandom.current().nextInt(14, 31);
            int randTown = ThreadLocalRandom.current().nextInt(0, 17);
            int randSchool = ThreadLocalRandom.current().nextInt(0, 17);
            System.out.print((j+1) + ", " + names.charAt(j) + ", " + randAge + ", " + towns[randTown] + ", " + schools[randSchool] + ", [");
            for (int k : e.getValue())
                System.out.print(k + ", ");
            System.out.println("]");
            j++;
        }
    }


}
