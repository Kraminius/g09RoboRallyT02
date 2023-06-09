package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;


public class ReaderAndWriter {

    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * writes a JSON at the file using a JSONObject found in the directory under the name parameter
     * @param name name of file
     * @param jsonObject the object to save
     */
    public void writeJSON(String name, JSONObject jsonObject, String type){
        File file = LocateJSONFile.getJSON(name, type);
        if(file == null){
            System.out.println("Cannot save json " + name + ". unable to find or create file.");
            return;
        }
        try{
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.close();
        }
        catch (Exception e){
            System.out.println("Error in writing JSON:\n"+e.getMessage());
        }
    }
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Reads a JSON file and returns a JSONObject
     * @param name reads a file under the name
     * @return JSONObject that contains the data from the file, null if there is an error.
     */
    public JSONObject readJSON(String name, String type) {
        try {
            File file = LocateJSONFile.getFile(name, type);
            if (file != null) {
                FileInputStream inputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                String jsonString = stringBuilder.toString();
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
                System.out.println("Loaded " + name + ".json file.");
                return jsonObject;
            } else {
                System.out.println(name + ".json file not found.");
                return null;
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading " + name + ".json file: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteFile(String name, String type){
        File file = LocateJSONFile.getFile(name, type);
        return file.delete();
    }
}
