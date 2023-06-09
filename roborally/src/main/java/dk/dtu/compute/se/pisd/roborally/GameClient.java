package dk.dtu.compute.se.pisd.roborally;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.*;

public class GameClient {



    public static ArrayList<String> players = new ArrayList<>();

    private static PlayerInfo playerInfo;


    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private static final int POLLING_INTERVAL_SECONDS = 5;

    //Tasks for polling. Can be closed
    private static ScheduledFuture<?> playerNamesPollingTask;
    private static ScheduledFuture<?> waitingForJoinButtonPress;

    private static ScheduledFuture<?> pickStartPosition;

    private static ScheduledFuture<?> allPickedStartPosition;
    private static ScheduledFuture<?> openedUpgradeShop;
    private static ScheduledFuture<?> updateAllPlayersUpgradeCards;

    //Waiting for start position
    public static void startWaitingForStartPosition(){
        pickStartPosition = executorService.scheduleAtFixedRate(GameClient::startPosition, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);;
    }

    public static void startAllPickedStartPosition(){
        allPickedStartPosition =executorService.scheduleAtFixedRate(GameClient::pollAllPickedStartPosition, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void startWaitingForOpenShop(){
        openedUpgradeShop = executorService.scheduleAtFixedRate(GameClient::pollOpenShop, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void startWaitingForUpgradeCards(){
        updateAllPlayersUpgradeCards =executorService.scheduleAtFixedRate(GameClient::pollUpgradeCards, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void pollUpgradeCards(){

        boolean temp;

        try {
            temp = allPlayersUpgraded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(temp){
            System.out.println("we are updating upgrade cards");
            javafx.application.Platform.runLater(() -> {
                RoboRally.getAppController().updateGame();
            });
            updateAllPlayersUpgradeCards.cancel(false);
        }
        else{
            System.out.println("we are waiting to update upgrade carads");
        }
    }

    public static void pollOpenShop(){

        boolean openShop;

        try {
            openShop = isUpgradeShopOpen();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int temp;
        try {
             temp = getCurrentPlayer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("this is temp: " + temp + " and this is myPlayerId; " + playerInfo.getPlayerId());

        if(openShop && temp == playerInfo.getPlayerId()){


            System.out.println("we have updated the shop");
            javafx.application.Platform.runLater(() -> {
                RoboRally.getAppController().updateGame();
            });



            openedUpgradeShop.cancel(false);


        }else {
            System.out.println("waiting for players before to buy");
        }

    }

    public static Boolean isUpgradeShopOpen() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/isUpgradeShopOpen"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        Boolean result = Boolean.valueOf(response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS));
        return result;
    }



    public static void pollAllPickedStartPosition(){


        boolean temp = false;
        try {
            temp = allPlayersPicked();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        if(temp){

            System.out.println("we have updated the game");
            javafx.application.Platform.runLater(() -> {
                        RoboRally.getAppController().updateGame(true);

                    });
            allPickedStartPosition.cancel(false);
        }else {
            System.out.println("We are waiting for all players to pick");
        }


    }

    public static boolean allPlayersPicked() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/allPlayersPicked"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return Boolean.parseBoolean(result);
    }



    public static void startPosition(){
        int player;
        try {
             player = getCurrentPlayer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(playerInfo.getPlayerId() == player){
            System.out.println("Yeah its my turn" + playerInfo.getPlayerId());
            javafx.application.Platform.runLater(() -> {
                ArrayList<Integer> liste;
                try {
                     liste = getStartPosition();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }

                RoboRally.getAppController().currentPlayersTurn(liste);
                pickStartPosition.cancel(false);
            });

        }

    }

    public static void picked() throws Exception{
        int number = playerInfo.getPlayerId();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/picked/" + number))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
    }

    public static int addStartPosition(int pos) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("pos=" + pos))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create("http://localhost:8080/addStartPosition"))
                .build();
        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return Integer.parseInt(result);
    }

    public static ArrayList<Integer> getStartPosition() throws InterruptedException, ExecutionException, TimeoutException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/getStartPosition"))
                .build();

        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

        Type listType = new TypeToken<ArrayList<Integer>>(){}.getType();

        return new Gson().fromJson(result, listType);
    }

    public static int getCurrentPlayer() throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/getCurrentPlayer"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return Integer.parseInt(result);
    }

    public static String nextPlayer() throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/nextPlayer"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result;
    }


    //Polling for how many players that have connected to the lobby
    public static void startPlayerNamesPolling() {

        playerNamesPollingTask = executorService.scheduleAtFixedRate(GameClient::pollPlayerNames, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }


    private static boolean pollAllConnected() throws Exception {
        Boolean allConnected = false;
        allConnected = areAllConnected();

        if(allConnected){
            System.out.println("We are all connected");
        }
        return allConnected;
    }


    private static void pollPlayerNames() {
        try {
            ArrayList<String> playerNames = getPlayerNames();

            if(players.size() == 0){
                players = playerNames;
            }



            if(players.size() != playerNames.size()){

                javafx.application.Platform.runLater(() -> {
                    GameLobby gameLobby = null;
                    try {
                        gameLobby = getGame();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    RoboRally.getLobby().updateJoinWindow(gameLobby);
                    players = playerNames;
                    });

            }



            // Process the player names as needed
            System.out.println("Player Names pulled: " + playerNames);

            if(pollAllConnected()){
                playerNamesPollingTask.cancel(false);
                if(playerInfo.getPlayerId() == 0){
                javafx.application.Platform.runLater(() -> {
                    System.out.println("this is the first player");
                    try {
                        RoboRally.getLobby().showStartButton(getLobbyId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });}
                else {
                    waitingForJoinButton();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that occur during polling
        }
    }

    public static void waitingForJoinButton(){
        waitingForJoinButtonPress = executorService.scheduleAtFixedRate(GameClient::pollJoinButton, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void pollJoinButton() {
        boolean temp = false;
        try {
            temp = isJoinButton();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Polling Join Button");

        if(temp){

            System.out.println("We are also starting now");
            waitingForJoinButtonPress.cancel(false);
            javafx.application.Platform.runLater(() -> {
                RoboRally.getLobby().startingGame();
            });
        }
    }


    public static String pressJoinButton() throws Exception{

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/pressJoinButton"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return "something";

    }

    public static boolean isJoinButton() throws Exception{

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/isJoinButton"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return Boolean.parseBoolean(result);
    }


    public static String getLobbyId() throws Exception{

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/getLobbyId"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result;
    }

    public static boolean weConnect(int playerNum, String name) throws Exception {
        // Create a JSON object

        playerInfo = new PlayerInfo(name, playerNum);

        JSONObject json = new JSONObject();
        json.put("playerNum", playerNum);
        json.put("name", name);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json.toString())) // send JSON object
                .header("Content-Type", "application/json")
                .uri(URI.create("http://localhost:8080/connected"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result.equals("we connected");
    }



    public static boolean areAllConnected() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/allConnected"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return Boolean.parseBoolean(result);
    }

    public static boolean instaGameData(int numberOfPlayers) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/instaGameData?numberOfPlayers=" + numberOfPlayers))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result.equals("something");
    }

    public static String addMapName(String mapName) throws Exception{
        String encodedMapName = URLEncoder.encode(mapName, StandardCharsets.UTF_8.toString());
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/addMapName?mapName=" + encodedMapName))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result;
    }

    public static String getMapName() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/getMapName"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result;
    }


    public static String addGame(String[] settings) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(settings);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/addGame"))
                .header("Content-Type", "application/json")
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result;
    }

    public static GameLobby getGame() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/getGameLobby"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

        // Parse JSON response into GameLobby object
        ObjectMapper objectMapper = new ObjectMapper();
        GameLobby gameLobby = objectMapper.readValue(result, GameLobby.class);

        return gameLobby;
    }

    public static Boolean isGameRunning() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/isGameRunning"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        Boolean result = Boolean.valueOf(response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS));
        return result;
    }

    public static ArrayList<String> getPlayerNames() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/getAllPlayers"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

        // Use ObjectMapper to convert the JSON string into an ArrayList<String>
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> result = objectMapper.readValue(jsonResponse, new TypeReference<ArrayList<String>>() {});

        return result;
    }

    public static int getPlayerNumber(){
        return playerInfo.getPlayerId();
    }


    public static void sendStartData(PlayerStartData playerStartData) throws Exception{

        int playerNumber = playerInfo.getPlayerId();

        System.out.println("it mus be here somewhere1");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(playerStartData);

        System.out.println("RequestBody: " + requestBody);
        System.out.println("it mus be here somewhere2");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/sendStartData/" + playerNumber))
                .build();

        System.out.println("it mus be here somewhere3");

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("it mus be here somewhere4");

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

    }


    public static void sendUpgradeCardsShop(Command[] cards, int nextPlayer, int currentPlayer) throws Exception{
        UpgradeCardsShopRequest requestObj = new UpgradeCardsShopRequest();
        requestObj.setCards(cards);
        requestObj.setNextPlayer(nextPlayer);
        requestObj.setCurrentPlayer(currentPlayer);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestObj);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/sendUpgradeCardsShop"))
                .header("Content-Type", "application/json")
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());



        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
    }

    public static void sendBoughtUpgradeCards(Command[] command) throws Exception{

        int playerNumber = playerInfo.getPlayerId();

        SendUpgradeCards playerUpgradeCards = new SendUpgradeCards();
        playerUpgradeCards.setCommands(command);
        playerUpgradeCards.setPlayerNumber(playerNumber);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(playerUpgradeCards);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/sendBoughtUpgradeCard"))
                .header("Content-Type", "application/json")
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

        startWaitingForUpgradeCards();
    }



    public static int turnNumber ()throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/turnNumber"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        int result = Integer.valueOf(response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS));
        return result;
    }

    public static Boolean allPlayersUpgraded() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/allPlayersUpgraded"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        Boolean result = Boolean.valueOf(response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS));
        return result;
    }

}
