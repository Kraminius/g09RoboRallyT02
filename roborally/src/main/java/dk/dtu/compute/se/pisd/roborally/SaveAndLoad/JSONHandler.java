    package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONHandler {

    ReaderAndWriter raw = new ReaderAndWriter();

    private void testJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "John Doe");
        jsonObject.put("age", 35);
        jsonObject.put("email", "john.doe@example.com");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("apple");
        jsonArray.add("banana");
        jsonArray.add("orange");
        jsonObject.put("fruits", jsonArray);

        raw.writeJSON("test",jsonObject);
        printJSON("test");
    }
    public JSONObject load(String name){
        return raw.readJSON(name);
    }
    public void save(String name, JSONObject json){
        raw.writeJSON(name, json);
    }
    public void printJSON(String name) {
        JSONObject jsonObject = raw.readJSON(name);
        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            Object keyVal = jsonObject.get(keyStr);
            if (keyVal instanceof JSONArray) {
                System.out.println(keyStr + ":");
                JSONArray jsonArray = (JSONArray) keyVal;
                for (Object item : jsonArray) {
                    System.out.println("  " + item);
                }
            } else {
                System.out.println(keyStr + ": " + keyVal);
            }
        }
    }




}
