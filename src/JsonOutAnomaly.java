import org.json.JSONException;
import org.json.JSONObject;

/**
 * Convert Jsonobject to String for output
 * JSON library java-json.jar was used for the JSONObject
 * Download from http://www.java2s.com/Code/JarDownload/java-json/java-json.jar.zip
 */

public class JsonOutAnomaly {

    public static String jsToStr(JSONObject jsin, String mean, String std) {
        String out = null;
        try {
            //String event = (String) jsin.get("event_type");
            String time = (String) jsin.get("timestamp");
            String id = (String) jsin.get("id");
            String amount = (String) jsin.get("amount");

            String head = "{\"event_type\":\"purchase\", \"timestamp\":\"";
            String subhead = "\", \"";
            String subtail = "\": \"";
            String tail = "\"}";
            out = head + time +
                    subhead + "id" + subtail + id +
                    subhead + "amount" + subtail + amount +
                    subhead + "mean" + subtail + mean +
                    subhead + "sd" + subtail + std + tail + "\n";

        }  catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }
}
