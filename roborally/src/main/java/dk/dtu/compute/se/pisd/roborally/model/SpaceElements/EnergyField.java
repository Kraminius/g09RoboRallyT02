package dk.dtu.compute.se.pisd.roborally.model.SpaceElements;

import javafx.scene.image.ImageView;

public class EnergyField {
    private int cubes;
    private ImageView energyCubeImage;

    public int getCubes() {
        return cubes;
    }

    public void setCubes(int cubes) {
        this.cubes = cubes;
        if(cubes > 0) setShowCube(true);
        else setShowCube(false);
    }

    public void setImageView(ImageView view){
        energyCubeImage = view;
        setCubes(1);
    }
    public void setShowCube(boolean show){
        if(energyCubeImage != null) {
            if(show) energyCubeImage.setOpacity(1);
            else energyCubeImage.setOpacity(0);
        }
    }
}
