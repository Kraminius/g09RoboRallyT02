package dk.dtu.compute.se.pisd.roborally;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Converter;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

public class MyClient {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();



    public static boolean weConnect(int playerNum) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(Integer.toString(playerNum)))
                .uri(URI.create("http://localhost:8080/connected"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result.equals("we connected");
    }

    public static String playerNumber() throws Exception{


        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/getPlayerNumber"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result;

    }




    private boolean connected;


    public boolean isConnected() {
        return connected;
    }


    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * A method used to call the server for the current gameState
     * @return a Load that can be loaded into the game to
     */
    public static Load update() {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/GetGame"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            Gson gson = new Gson();
            Load load = gson.fromJson(result, Load.class);
            return load;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param obj - A JSONObject that has info from a a json file of a new game.
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * Creates a Load.java object that can hold all the data from a new load,
     * so all data can be pulled from this file already being converted to the right format.
     * The method uses the Converter.java class to convert after receiving the data from the obj file.
     */
    private static Load loadData(JSONObject obj){
        Load load = new Load();
        load.setBoard((String)obj.get("board"));
        load.setStep(parseInt(String.valueOf(obj.get("step"))));
        load.setCurrentPlayer((String)obj.get("currentPlayer"));
        load.setStepmode(Converter.getBool((String)obj.get("isStepMode")));
        load.setPhase(Converter.getPhase ((String) obj.get("phase")));
        load.setPlayerAmount(parseInt(String.valueOf(obj.get("playerAmount"))));
        int amount = load.getPlayerAmount();
        load.setPlayerNames(new String[amount]);
        load.setPlayerColors(new String[amount]);
        load.setPlayersXPosition(new int[amount]);
        load.setPlayersYPosition(new int[amount]);
        load.setPlayerEnergyCubes(new int[amount]);
        load.setPlayerCheckPoints(new int[amount]);
        load.setPlayerHeadings(new Heading[amount]);
        load.setPlayerProgrammingDeck(new Command[amount][]);
        load.setPlayerCurrentProgram(new Command[amount][]);
        load.setPlayerDiscardPile(new Command[amount][]);
        load.setPlayerUpgradeCards(new Command[amount][]);
        load.setPlayersPulledCards(new Command[amount][]);
        String[][] playersProgrammingDeck  = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersProgrammingDeck")), "#");
        String[][] playersProgram  = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersProgram")), "#");
        String[][] playerUpgradeCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playerUpgradeCards")), "#");
        String[][] playersDiscardCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersDiscardCards")), "#");
        String[][] playersPulledCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersPulledCards")), "#");
        for(int i = 0; i < amount; i++){
            load.getPlayerNames()[i] = Converter.jsonArrToString((JSONArray)obj.get("playersName"))[i];
            load.getPlayerColors()[i] = Converter.jsonArrToString((JSONArray)obj.get("playerColor"))[i];
            load.getX()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersX"))[i];
            load.getY()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersY"))[i];
            load.getPlayerEnergyCubes()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playerCubes"))[i];
            load.getPlayerHeadings()[i] = Converter.getHeading (Converter.jsonArrToString((JSONArray)obj.get("playersHeading"))[i]);
            load.getPlayerCheckPoints()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersCheckpoints"))[i];
            load.getPlayerProgrammingDeck()[i] = Converter.getCommands(playersProgrammingDeck[i]);
            load.getPlayerCurrentProgram()[i] = Converter.getCommands(playersProgram[i]);
            load.getPlayerUpgradeCards()[i] = Converter.getCommands(playerUpgradeCards[i]);
            load.getPlayerDiscardPile()[i] = Converter.getCommands(playersDiscardCards[i]);
            load.getPlayersPulledCards()[i] = Converter.getCommands(playersPulledCards[i]);
        }
        load.setMapCubePositions(Converter.jsonArrToInt((JSONArray)obj.get("mapCubes")));
        load.setUpgradeCardsDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeCardsDeck"))));
        load.setUpgradeOutDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeOutDeck"))));
        load.setUpgradeDiscardDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeDiscardDeck"))));
        return load;
    }

    //Sending a game
    public static boolean sendGame(JSONObject json) throws Exception {
        String jsonString = json.toString(); // Convert JSONObject to string
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .uri(URI.create("http://localhost:8080/sendingGame"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result.equals("acknowledged");
    }


    public static boolean instantiateGame(Load load) throws Exception {
        String productJSON = new Gson().toJson(load);
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(productJSON))
                .uri(URI.create("http://localhost:8080/instaGameState"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result.equals("instantiated");
    }


}
