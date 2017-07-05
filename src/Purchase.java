/**
 * Purchase is a class to represent one purchase record.
 * Each purchase record has purchase amount and the purchase time, which is recorded as the log number
 *(1) when two logs occur at the different time, large indexlog number means latter time
 *(2) when two logs occur at the same time by different order in log file, indexlog represent latter log
 */

public class Purchase {
    private float amount;
    private int indexlog;

    public Purchase(float amount, int indexlog) {
        this(amount, indexlog, "no ID");
    }
    public Purchase(float amount, int indexlog, String id) {
        this.amount = amount;
        this.indexlog = indexlog;
    }
    public float getAmount() {
        return amount;
    }

    public int getIndexlog(){
        return indexlog;
    }

}
