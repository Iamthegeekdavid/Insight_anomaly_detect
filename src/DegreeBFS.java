import java.util.*;

/**
 * class DegreeBFS: degree-depended breath-first search (DegreeBFS) algorithm
 * Methods:
 * (1) purchaseBFS: return all neighbors' purchases information (amount + time) with maxdegree connection of the root user
 * (2) cleanVisited: mark all users to the un-visted status when DegreeBFS is done for the root user
 */

//Degree definition shown in the following graph
//A  - B
//|
//C - D - E
//B is a first-degree neighbor of A
//D is a second-degree neighbor of A
//E is a 3rd-degree neighbor of A

public class DegreeBFS {

    //Degree-depended breath-first search (DegreeBFS) algorithm
    public static List<List<Purchase>> purchaseBFS(User root, int maxdegree) {
        List<User> cur_neighbor = root.neighbor;
        List<User> next_neigbhor;
        List<List<User>> result = new LinkedList<>();
        List<List<Purchase>> purchaserecord = new LinkedList<>();

        //add the first degree (direct neighbors) to result
        //and mark all first degree users as visited
        //maxdegree-- because BFS finishes the first-degree users
        root.Visited = true;
        result.add(cur_neighbor);
        for (User cur: cur_neighbor) {
            purchaserecord.add(cur.getPurchase());
            cur.Visited = true;
        }
        maxdegree--;

        while (maxdegree > 0 ) {
            next_neigbhor = new LinkedList<>();
            for (User nei: cur_neighbor) {
                for (User next: nei.neighbor) {
                    if (!next.Visited) {
                        next.Visited = true;
                        purchaserecord.add(next.getPurchase());
                        next_neigbhor.add(next);
                    }
                }
            }
            result.add(next_neigbhor);
            cur_neighbor = next_neigbhor;
            next_neigbhor = null;
            //maxdegree decreases by 1 as BFS finishes all users at current layer
            maxdegree--;
        }
        root.Visited = false;
        cleanVisited(result);
        return purchaserecord;
    }

    //Mark all users to the un-visted status when Degree-depened BFS is done
    public static void cleanVisited(List<List<User>> userlist) {
        for (List<User> onelist: userlist) {
            for (User user: onelist) {
                user.Visited = false;
            }
        }
    }
}
