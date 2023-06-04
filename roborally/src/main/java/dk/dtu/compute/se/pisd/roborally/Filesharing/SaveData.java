package dk.dtu.compute.se.pisd.roborally.Filesharing;

import java.io.File;
import java.util.ArrayList;

public class SaveData {

    private int id;
    private int version;
    private String name;
    private File file;

    public SaveData(int id, File file){
        this.id = id;
        this.file = file;
        this.version = 0;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
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
