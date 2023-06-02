package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

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

        raw.writeJSON("test", jsonObject, "board");
        printJSON("test", "board");
    }
    public String[] getNamesOfGame(String name){
        JSONObject obj = raw.readJSON(name, "game");
        String[] names = Converter.jsonArrToString((JSONArray)obj.get("playersName"));
        return names;
    }

    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Loads a json file and recieves a JSON object.
     * @param name the name of the file it should look for
     * @return an JSONObject if the file is found, if not it returns null.
     */
    public JSONObject load(String name, String type){
        return raw.readJSON(name, type);
    }
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Saves a json object under a name into a file in the directory.
     * If it already exist it will overwrite it.
     * @param json the json Object that needs to be saved
     * @param name the name the file should be saved under or overwrite
     */
    public void save(String name, JSONObject json, String type){
        raw.writeJSON(name, json, type);
    }
    public void printJSON(String name, String type) {
        JSONObject jsonObject = raw.readJSON(name, type);
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
