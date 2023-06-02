package dk.dtu.compute.se.pisd.roborally.Filesharing;

import java.io.File;
import java.util.ArrayList;

public class SaveData {

    private int id;
    private int version;
    private String name;
    private File file;

    public SaveData(int id, String name, File file){
        this.id = id;
        this.name = name;
        this.file = file;
        this.version = 0;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public File getFile(){
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public int getVersion(){
        return version;
    }
    public void setVersion(int version){
        this.version = version;
    }
    public void nextVersion(){
        version += 1;
    }
}
