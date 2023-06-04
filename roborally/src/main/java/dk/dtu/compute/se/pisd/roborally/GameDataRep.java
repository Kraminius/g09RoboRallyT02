package dk.dtu.compute.se.pisd.roborally;


import dk.dtu.compute.se.pisd.roborally.model.GameLobby;
import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import org.springframework.stereotype.Service;

@Service
public class GameDataRep {


    GameData gameData;

    public GameDataRep(){

    }

    public void instantiateGameData(int numPlayer){
        this.gameData = new GameData(numPlayer);
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

    public int playerNumber(){

        int i;
        for (i = 0; i < gameData.readyList.length; i++) {

            System.out.println("This player: " + gameData.readyList[i]);

            if(!gameData.readyList[i]){
                break;
            }
        }

        return i;
    }








}
