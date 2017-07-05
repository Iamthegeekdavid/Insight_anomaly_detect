
import java.util.*;

/**
 * User is a class to represent users of the social network at Market-ter Inc.
 * User has userID (String) and purchaseRecord (LinkedList).
 */

public class User {
    private final String userID;
    //batch_login.json is ordered by time
    //use log index rather than time here
    //Reason: (1) when two logs occur at the different time, large indexlog number means latter time
    //        (2) when two logs occur at the same time by different order in log file,
    private List<Purchase> purchaseRecord;

    //first-degree neighbors
    public List<User> neighbor;
    //Visited to indicate that a user has been visited or not when doing the degree-depended Breadth-First Search
    public boolean Visited = false;

    public User(String userID) {
        this.userID = userID;
        neighbor = new LinkedList<>();
        purchaseRecord = new LinkedList<>();
    }
    public void newPurchase(float amount, int indexlog) {
            //It is assumed that input has been sorted by time as shown by batch_log.json
            purchaseRecord.add(new Purchase(amount, indexlog, userID));
    }
    public String getUserID() {
        return userID;
    }
    public List<Purchase> getPurchase() {
        return purchaseRecord;
    }

    public void beFriend(User user) {
        if (user != null)
            neighbor.add(user);
        else {
            throw new IllegalArgumentException("USER ID problem in beFriend method ");
        }
    }

    public void unFriend(User user) {
        if (user != null)
            neighbor.remove(user);
        else {
            throw new IllegalArgumentException("USER ID problem in unFriend method ");
        }
    }
}
