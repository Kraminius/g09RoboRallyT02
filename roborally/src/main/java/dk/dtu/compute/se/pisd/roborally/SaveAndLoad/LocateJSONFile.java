package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class LocateJSONFile {
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Gets a file from a name if it exists.
     * @param name name of the file
     * @return returns the file if exists, otherwise null.
     */
    public static File getFile(String name, String type){
        File directory = null;
        if(type.equals( "board")) directory = new File("roborally/src/main/resources/boards/");
        if(type.equals("game")) directory = new File("roborally/src/main/resources/games/");
        if(directory == null) {
            System.out.println("No such type");
            return null;
        }
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
    public static File overWriteOrCreate(String name, String type) {
        try {
            File directory = null;
            if(type.equals( "board")) directory = new File("roborally/src/main/resources/boards/");
            else if(type.equals("game")) directory = new File("roborally/src/main/resources/games/");
            else if(type.equals("saveData")) directory = new File("roborally/src/main/resources/saveData/");
            if(directory == null) {
                System.out.println("No such type");
                return null;
            }
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
    public static JSONObject createJSON(File file){
        try {
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
                return jsonObject;
            } else {
                return null;
            }
        } catch (IOException | ParseException e) {
            return null;
        }
    }
}
