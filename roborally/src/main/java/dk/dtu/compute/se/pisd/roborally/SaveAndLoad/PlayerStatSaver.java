package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class PlayerStatSaver {


    JSONHandler json = new JSONHandler();
    ReaderAndWriter raw = new ReaderAndWriter();

    JSONObject obj = new JSONObject();

    public void setPlayerStat(Board board, Player player){

        for(int i = 0; i < board.getPlayersNumber(); i++){
            Player player = board.getPlayer(i);
            obj.put("name", player.getName());
            obj.put("color", player.getColor());
            obj.put("space", player.getSpace());
            obj.put("heading", player.getHeading());
            //Denne her array del skal testes
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < board.getNumOfCheckpoints(); i ++){
                jsonArray.add(player.getCheckpointReadhed()[i]);
            }
            obj.put("checkpoint", jsonArray);
        }

        obj.put("name", player)

        json.save("PlayerStat", );

        json.put("name", "John Doe");
        json.put("age", 35);
        json.put("email", "john.doe@example.com");
        JSONArray jsonArray = new JSONArray();

        raw.writeJSON("PlayerStat",jsonObject);


        JSONObject obj = json.writeJSON("PlayerStat" + id);
        writeJSON

    }

}
