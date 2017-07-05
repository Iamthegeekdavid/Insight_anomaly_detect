import java.util.List;

/**
 * TrackedPurchase class is to find the defined number of purchases from latest to oldest from all friends' purchases  
 */

public class TrackedPurchase {
	
	//convert LinkedList purchase to purchase array
    public static Purchase[][] convertArray(List<List<Purchase>> purchaselist) {
        Purchase[][] purchaseArray = new Purchase[purchaselist.size()][];
        int indexrow = 0;
        for (List<Purchase> onelist: purchaselist) {
            purchaseArray[indexrow] = new Purchase[onelist.size()];
            int indexcol = 0;
            for (Purchase onepurchase: onelist) {
                purchaseArray[indexrow][indexcol++] = onepurchase;
            }
            indexrow++;
        }
        return purchaseArray;
    }
    //find the defined number of purchases
    public static float [] trackMerge(Purchase[][] array, int track) {
        //track define the size of returned array for anomaly detection
        float[] trackArray = new float[track];
        int rows = array.length;
        //corner case: rows == 0 or cols == 0
        if (rows == 0) {
            return new float[0];
        }
        //find col number at each row and return if max cols == 0
        //counter of total number of purchases from all A's neighbors
        int count = 0;
        int maxcols = 0;
        int[] pointer = new int[rows];
        //pointer starts from the end of the purchaseArray
        //because purchaseArray is sorted by time increase
        for (int r = 0; r < rows; r++) {
            pointer[r] = array[r].length - 1;
            count = count + pointer[r];
            maxcols = (pointer[r] > maxcols) ? pointer[r] : maxcols;
        }
        if(maxcols == 0) {
            return new float[0];
        }

        int index = 0;
        int rowmerge = 0;
        while (index < count) {
            for (int r = 0; r < rows; r++) {
                if (pointer[r] >= 0) {
                    if (pointer[rowmerge] >= 0) {
                        rowmerge =
                                (array[rowmerge][pointer[rowmerge]].getIndexlog() >
                                        array[r][pointer[r]].getIndexlog()) ? rowmerge : r;
                    } else {
                        rowmerge = r;
                    }
                }
            }
            if (pointer[rowmerge] >= 0) {
                trackArray[index++] = array[rowmerge][pointer[rowmerge]].getAmount();
            }
            if (index >= track) {
                break;
            }
            //pointer moves one step towords right
            pointer[rowmerge]--;
        }
        return trackArray;
    }
}
