package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;


public class ReaderAndWriter {
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Gets a file from a name if it exists.
     * @param name name of the file
     * @return returns the file if exists, otherwise null.
     */
    public File getFile(String name){
        File directory = new File("roborally/src/main/resources/files/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, name + ".json");
        if (file.exists()) return file;
        else return null;
    }

    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Looks for the file in a directory and creates it if it doesn't exist already.
     * @param name the name of the file
     * @return a File under that name, null if there is an error in creating it.
     */
    public File createIfNonExistingJSON(String name) {
        try {
            File directory = new File("roborally/src/main/resources/files/");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, name + ".json");
            if (!file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("{}"); // Empty JSON object
                fileWriter.close();
                System.out.println("Created "+name+".json file at " + file.getAbsolutePath());
            }
            file = new File(directory, name + ".json");
            if(file.exists()){
                return file;
            }
            else{
                System.out.println("Error in creating file. No return file.");
            }
        } catch (IOException e) {
            System.out.println("Error creating "+name+".json file: " + e.getMessage());
        }
        return null;
    }
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * writes a JSON at the file using a JSONObject found in the directory under the name parameter
     * @param name name of file
     * @param jsonObject the object to save
     */
    public void writeJSON(String name, JSONObject jsonObject){
        File file = createIfNonExistingJSON(name);
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
    public JSONObject readJSON(String name) {
        try {
            File file = getFile(name);
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
}
