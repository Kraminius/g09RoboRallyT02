package dk.dtu.compute.se.pisd.roborally;


import org.springframework.stereotype.Service;

@Service
public class GameDataRep {


    GameData gameData;

    public GameDataRep(){
        gameData = new GameData(4);
    }



    public boolean checkerPlayersConnected(){

        for (int i = 0; i < gameData.readyList.length; i++) {

            System.out.println("This player: " + gameData.readyList[i]);

            if(!gameData.readyList[i]){
                return false;
            }
        }

        return true;

    }








}
