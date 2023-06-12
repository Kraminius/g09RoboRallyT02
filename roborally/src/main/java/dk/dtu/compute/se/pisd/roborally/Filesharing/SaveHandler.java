package dk.dtu.compute.se.pisd.roborally.Filesharing;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.LocateJSONFile;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.ReaderAndWriter;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
@RestController
public class SaveHandler {


    private ArrayList<SaveData> saveData = new ArrayList<>();
    private ReaderAndWriter permData = new ReaderAndWriter();
    private AppController appController;
    private RoboRally main;
    /*public SaveHandler(AppController appController, RoboRally main){
        this.appController = appController;
        this.main = main;
    }*/

    /*@GetMapping("/data/{id}")
    public File getData(int id){
        for(SaveData data : saveData){
            if(data.getId() == id) return data.getFile();
        }
        System.out.println("ID not found");
        return null;
    }
    @PostMapping("/data/{id, file}")
    public boolean addData(int id, File file){
        SaveData newSaveData = new SaveData(id, file);

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
    @PutMapping("/data/{id, file}")
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
    @DeleteMapping("/data/{id}")
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
    @DeleteMapping("/data")
    public void clearSessionData(){
        saveData.clear();
    }*/
    public void permSave(int id){
        SaveData data = saveData.get(getIndexOfData(id));
        permData.writeJSON(data.getId()+"", LocateJSONFile.createJSON(data.getFile()), "saveData");
    }
    public File loadFromPermanent(int id){
        try{
            return new File("roborally/src/main/resources/saveData/" + id);
        }catch (Exception e){
            System.out.println("Cannot find the file with id=" + id);
            return null;
        }
    }
    public void loadGame(String name){
        try {
            appController.loadGame();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getIndexOfData(int id){
        for(SaveData data : saveData){
            if(data.getId() == id) return saveData.indexOf(data);
        }
        return -1;
    }


}
