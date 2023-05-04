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
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Loads a json file and recieves a JSON object.
     * @param name the name of the file it should look for
     * @return an JSONObject if the file is found, if not it returns null.
     */
    public JSONObject load(String name){
        return raw.readJSON(name);
    }
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Saves a json object under a name into a file in the directory.
     * If it already exist it will overwrite it.
     * @param json the json Object that needs to be saved
     * @param name the name the file should be saved under or overwrite
     */
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
