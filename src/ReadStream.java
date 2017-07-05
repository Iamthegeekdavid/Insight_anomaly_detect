import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Read the batch_log and stream_log 
 * Output the purchase record in stream log if it is an anomaly 
 * JSON library java-json.jar was used for the JSONObject
 * It was be downloaded from http://www.java2s.com/Code/JarDownload/java-json/java-json.jar.zip
 */

public class ReadStream {

    public static void main(String[] args) {

        String path = "./log_input/batch_log.json";
        String pathstream = "./log_input/stream_log.json";
        String anomalyfile = "./log_output/flagged_purchases.json";
        
        if (args.length == 3) {
            path = args[0];
            pathstream = args[1];
            anomalyfile =args[2];
            System.out.println("Batch path: " + args[0] + " Stream path: " + args[1] + " Anomaly path: " + args[2]);
        }

        String linestream = null;
        String line = null;

        Map<String,User> dict = new HashMap<>();

        //maxdegree and tracknumber will be assigned to new values from batch_log.json
        int maxdegree = 0;
        int tracknumber = 0;

        //dict is a HashMap to save all pairs of ID name (key) to ID class (value)
        //to count lines of batch log file
        //Integer is big enough  to save index of batch_log (size: 500,000 lines)
        //Long is needed for date in case scaling up the size of the project
        int indexlog = 1;
        
        //read batch_log file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            line = reader.readLine();
            JSONObject json = new JSONObject(line);
            maxdegree = Integer.parseInt((String) json.get("D"));
            tracknumber = Integer.parseInt((String) json.get("T"));

            while ((line = reader.readLine()) != null) {
                indexlog++;
                //in case linestream is empty line or not json format
                if (line.equals("") || line.charAt(0) != '{') {
                    continue;
                }
                json = new JSONObject(line);
                String event = (String) json.get("event_type");
                if (event.equals("purchase")) {
                    String purchaseID = (String) json.get("id");
                    String purchaseAmount = (String) json.get("amount");
                    float amount = Float.parseFloat(purchaseAmount);
                    if (!dict.containsKey(purchaseID)) {
                    	  dict.put(purchaseID, new User(purchaseID));
                    } else {
                        dict.get(purchaseID).newPurchase(amount, indexlog);
                    }
                } else if(event.equals("befriend")) {
                    String id1 = (String) json.get("id1");
                    String id2 = (String) json.get("id2");
                    if (!dict.containsKey(id1)) {
                    	dict.put(id1, new User(id1));
                    }
                    if (!dict.containsKey(id2)) {
                    	dict.put(id2, new User(id2));
                    }
                    dict.get(id1).beFriend(dict.get(id2));
                    dict.get(id2).beFriend(dict.get(id1));
                } else if(event.equals("unfriend")) {
                    String id1 = (String) json.get("id1");
                    String id2 = (String) json.get("id2");
                    if (!dict.containsKey(id1)) {
                    	dict.put(id1, new User(id1));
                    }
                    if (!dict.containsKey(id2)) {
                    	dict.put(id2, new User(id2));
                    }
                    dict.get(id1).unFriend(dict.get(id2));
                    dict.get(id2).unFriend(dict.get(id1));
                } else {
                    System.out.println(line);
                    throw new IllegalArgumentException("unknown format data read");
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

      //read stream_log and generate flagged_purchase file
        try {
            BufferedReader readerstream = new BufferedReader(new FileReader(pathstream));
            BufferedWriter anomlaywrite = new BufferedWriter( new FileWriter(anomalyfile));;
            while ((linestream = readerstream.readLine()) != null) {
                indexlog++;
                //in case linestream is empty line or not json format
                if (linestream.equals("") || linestream.charAt(0) != '{') {
                    continue;
                }
                JSONObject json = new JSONObject(linestream);
                String event = (String) json.get("event_type");

                if (event.equals("purchase")) {
                    String purchaseID = (String) json.get("id");
                    String purchaseAmount = (String) json.get("amount");
                    float amount = Float.parseFloat(purchaseAmount);
                    if (!dict.containsKey(purchaseID)) {
                    	dict.put(purchaseID, new User(purchaseID));
                    } else {
                        List<List<Purchase>> outList = DegreeBFS.purchaseBFS(dict.get(purchaseID), maxdegree);
                        Purchase[][] outArray = TrackedPurchase.convertArray(outList);
                        float[] trackarray = TrackedPurchase.trackMerge(outArray, tracknumber);
                        //detect if anomaly
                        if (Anomaly.detect(amount, trackarray)) {
                            String strmean = String.format("%.02f", Anomaly.getMean(trackarray));
                            String strstd = String.format("%.02f", Anomaly.getStd(trackarray));
                            //write out flagged purchases
                            anomlaywrite.write(JsonOutAnomaly.jsToStr(json, strmean, strstd));
                        }
                        dict.get(purchaseID).newPurchase(amount, indexlog);
                    }
                } else if(event.equals("befriend")) {
                    String id1 = (String) json.get("id1");
                    String id2 = (String) json.get("id2");
                    if (!dict.containsKey(id1)) {
                    	dict.put(id1, new User(id1));
                    }
                    if (!dict.containsKey(id2)) {
                    	dict.put(id2, new User(id2));
                    }
                    dict.get(id1).beFriend(dict.get(id2));
                    dict.get(id2).beFriend(dict.get(id1));
                } else if(event.equals("unfriend")) {
                    String id1 = (String) json.get("id1");
                    String id2 = (String) json.get("id2");
                    if (!dict.containsKey(id1)) {
                    	dict.put(id1, new User(id1));
                    }
                    if (!dict.containsKey(id2)) {
                    	dict.put(id2, new User(id2));
                    }
                    dict.get(id1).unFriend(dict.get(id2));
                    dict.get(id2).unFriend(dict.get(id1));
                } else {
                    System.out.println(line);
                    throw new IllegalArgumentException("unknown format data read");
                }
            }
            anomlaywrite.close();
            readerstream.close();
        } catch (IOException e) {
            System.out.println("empty line read");
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
