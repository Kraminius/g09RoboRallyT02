package dk.dtu.compute.se.pisd.roborally.Filesharing;

import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.ReaderAndWriter;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class SaveHandler {


    private ArrayList<SaveData> saveData = new ArrayList<>();
    private ReaderAndWriter permData = new ReaderAndWriter();

    public File getData(int id){
        for(SaveData data : saveData){
            if(data.getId() == id) return data.getFile();
        }
        System.out.println("ID not found");
        return null;
    }
    public boolean addData(SaveData newSaveData){
        boolean exists = (getIndexOfData(newSaveData.getId()) != -1);
        if(exists){
            System.out.println("SaveData with that id, aready exists");
            return false;
        }
        else{
            saveData.add(newSaveData);
            return true;
        }
    }
    public boolean updateData(int id, File file){
        int position = getIndexOfData(id);
        if(position == -1){
            System.out.println("No file with the same id exists. SaveData cannot be updated");
            return false;
        }
        else{
            saveData.get(position).setFile(file);
            saveData.get(position).nextVersion();
            return true;
        }

    }
    public boolean deleteData(int id){
        int position = getIndexOfData(id);
        if(position != -1){
            saveData.remove(position);
            return true;
        }
        else{
            System.out.println("No file with the same id exists. SaveData cannot be deleted");
            return false;
        }
    }
    public void clearSessionData(){
        saveData.clear();
    }
    public void permSave(int id){
        SaveData data = saveData.get(getIndexOfData(id));
        permData.writeJSON(data.getId()+";=;"+data.getName(), permData.createJSON(data.getFile()), "saveData");
    }
    public SaveData loadFromPermanent(int id, String name){
        try{
            File file = new File("roborally/src/main/resources/saveData/" + id + ";=;" + name);
            SaveData newSaveData = new SaveData(id, name, file);
            return newSaveData;
        }catch (Exception e){
            System.out.println("Cannot find the file with id=" + id + ", and name="+name);
            return null;
        }
    }
    private int getIndexOfData(int id){
        for(SaveData data : saveData){
            if(data.getId() == id) return saveData.indexOf(data);
        }
        return -1;
    }


}
