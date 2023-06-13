package dk.dtu.compute.se.pisd.roborally;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.chat.ClientInfo;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class GameClient {


    public static ArrayList<String> players = new ArrayList<>();

    private static PlayerInfo playerInfo;


    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private static final int POLLING_INTERVAL_SECONDS = 2;

    //Tasks for polling. Can be closed
    private static ScheduledFuture<?> playerNamesPollingTask;
    private static ScheduledFuture<?> waitingForJoinButtonPress;

    private static ScheduledFuture<?> pickStartPosition;

    private static ScheduledFuture<?> allPickedStartPosition;
    private static ScheduledFuture<?> openedUpgradeShop;
    private static ScheduledFuture<?> updateAllPlayersUpgradeCards;
    private static ScheduledFuture<?> waitingForAllPlayersToPickCards;
    private static ScheduledFuture<?> waitingForAllPlayersToExecute;
    private static ScheduledFuture<?> waitingForInteractive;

    //Waiting for start position
    public static void startWaitingForStartPosition() {
        pickStartPosition = executorService.scheduleAtFixedRate(GameClient::startPosition, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void startWaitingForInteractive(){
        waitingForInteractive = executorService.scheduleAtFixedRate(GameClient::pollInteractive, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void startWaitingForExecution() {
        waitingForAllPlayersToExecute = executorService.scheduleAtFixedRate(GameClient::pollForExecution, 2, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void startAllPickedStartPosition() {
        allPickedStartPosition = executorService.scheduleAtFixedRate(GameClient::pollAllPickedStartPosition, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void startWaitingForOpenShop() {
        openedUpgradeShop = executorService.scheduleAtFixedRate(GameClient::pollOpenShop, 2, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void startWaitingForUpgradeCards() {
        updateAllPlayersUpgradeCards = executorService.scheduleAtFixedRate(GameClient::pollUpgradeCards, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void startWaitingForAllPlayersToPickCards() {
        waitingForAllPlayersToPickCards = executorService.scheduleAtFixedRate(GameClient::pollWaitingForPickedCards, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void pollInteractive(){

        boolean playerChoice;

        try {
            playerChoice = isPlayerChoice();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(playerChoice){

            System.out.println("we have updated interactive");

            try {

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            javafx.application.Platform.runLater(() -> {
                RoboRally.getAppController().updateGame();
            });


            waitingForInteractive.cancel(false);


        }else {
            System.out.println("We are waiting for player to use interactive card");
        }



    }

    public static void pollForExecution() {

        System.out.println("går vi i gang");

        boolean temp;

        try {
            temp = areAllConnected();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (temp) {

            try {
                changePhase(Phase.UPGRADE);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("WE ARE DONE WITH EXE");
            System.out.println("all players have executed");
            javafx.application.Platform.runLater(() -> {
                RoboRally.getAppController().updateGame();
            });

            try {
                readyToReset();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int currId;
            try {
                currId = currentUpgradePlayer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if(currId != playerInfo.getPlayerId()){
                startWaitingForOpenShop();
            }
            else {
                RoboRally.getInstance().getBoardView().getPlayersView().activateUpgradeButton(true);
            }

            waitingForAllPlayersToExecute.cancel(false);



        } else {
            System.out.println("we are waiting for all player to execute");
        }


    }

    public static void pollWaitingForPickedCards() {
        boolean temp;


        try {
            temp = areAllConnected();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (temp) {

            try {
                changePhase(Phase.ACTIVATION);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            javafx.application.Platform.runLater(() -> {
                RoboRally.getAppController().updateGame();
            });


            try {
                readyToReset();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            startWaitingForExecution();

            RoboRally.getAppController().getGameController().finishProgrammingPhase2();
            waitingForAllPlayersToPickCards.cancel(false);
        } else {
            RoboRally.getInstance().getBoardView().getPlayersView().activateWaitingGif(true);
        }


    }

    public static void pollUpgradeCards() {

        boolean temp;

        try {
            temp = allPlayersUpgraded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (temp) {
            System.out.println("we are updating upgrade cards");

            try {
                changePhase(Phase.PROGRAMMING);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            javafx.application.Platform.runLater(() -> {
                RoboRally.getAppController().updateGame();
                //RoboRally.getAppController().getGameController().startProgrammingPhase();
            });



            updateAllPlayersUpgradeCards.cancel(false);
        } else {
            System.out.println("we are waiting to update upgrade carads");
        }
    }

    public static void pollOpenShop() {

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

        System.out.println("open;" + openShop);

        System.out.println("this is temp: " + temp + " and this is myPlayerId; " + playerInfo.getPlayerId());

        if (openShop && temp == playerInfo.getPlayerId()) {


            System.out.println("we have updated the shop");
            javafx.application.Platform.runLater(() -> {
                RoboRally.getAppController().updateGame();
            });


            openedUpgradeShop.cancel(false);


        } else {
            RoboRally.getInstance().getBoardView().getPlayersView().activateUpgradeButton(false);
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


    public static void pollAllPickedStartPosition() {


        boolean temp = false;
        try {
            temp = allPlayersPicked();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (temp) {

            System.out.println("we have updated the game");
            javafx.application.Platform.runLater(() -> {
                RoboRally.getAppController().updateGame(true);
                //RoboRally.getAppController().getGameController().startProgrammingPhase();
            });
            allPickedStartPosition.cancel(false);
        } else {
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


    public static void startPosition() {
        int player;
        try {
            player = getCurrentPlayer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (playerInfo.getPlayerId() == player) {
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

    public static void picked() throws Exception {
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

        Type listType = new TypeToken<ArrayList<Integer>>() {
        }.getType();

        return new Gson().fromJson(result, listType);
    }

    public static int getCurrentPlayer() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/getCurrentPlayer"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return Integer.parseInt(result);
    }

    public static String nextPlayer() throws Exception {
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

        if (allConnected) {
            System.out.println("We are all connected");
        }
        return allConnected;
    }


    private static void pollPlayerNames() {
        try {
            ArrayList<String> playerNames = getPlayerNames();

            if (players.size() == 0) {
                players = playerNames;
            }


            if (players.size() != playerNames.size()) {

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

            if (pollAllConnected()) {
                playerNamesPollingTask.cancel(false);
                if (playerInfo.getPlayerId() == 0) {
                    javafx.application.Platform.runLater(() -> {
                        System.out.println("this is the first player");
                        try {
                            RoboRally.getLobby().showStartButton(getLobbyId());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    waitingForJoinButton();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that occur during polling
        }
    }

    public static void waitingForJoinButton() {
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

        if (temp) {

            System.out.println("We are also starting now");
            waitingForJoinButtonPress.cancel(false);

            boolean loaded;

            try {
                loaded = isLoadedGame();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (loaded) {
                javafx.application.Platform.runLater(() -> {
                    RoboRally.getLobby().startingGame(true);
                });
            }
            else{
                javafx.application.Platform.runLater(() -> {
                    RoboRally.getLobby().startingGame(false);
                });
            }

        }
    }


    public static String pressJoinButton() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/pressJoinButton"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return "something";

    }

    public static boolean isJoinButton() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/isJoinButton"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return Boolean.parseBoolean(result);
    }


    public static String getLobbyId() throws Exception {

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

    public static boolean instaGameData(int numberOfPlayers) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/instaGameData?numberOfPlayers=" + numberOfPlayers))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result.equals("something");
    }

    public static String addMapName(String mapName) throws Exception {
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


    public static String addGame(String[] settings, boolean loadedGame) throws Exception {
        // Convert settings array to JSON Array
        String settingsJsonArray = new ObjectMapper().writeValueAsString(settings);

        // Prepare the requestBody
        String requestBody = String.format("{\"settings\": %s, \"loadedGame\": %s}", settingsJsonArray, loadedGame);

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
        ArrayList<String> result = objectMapper.readValue(jsonResponse, new TypeReference<ArrayList<String>>() {
        });

        return result;
    }

    public static int getPlayerNumber() {
        return playerInfo.getPlayerId();
    }


    public static void sendStartData(PlayerStartData playerStartData) throws Exception {

        int playerNumber = playerInfo.getPlayerId();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(playerStartData);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/sendStartData/" + playerNumber))
                .build();


        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());


        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

    }


    public static void sendUpgradeCardsShop(Command[] cards, int nextPlayer, int currentPlayer) throws Exception {
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

    public static void sendBoughtUpgradeCards(Command[] command) throws Exception {

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


    public static int turnNumber() throws Exception {
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

    public static void changePhase(Phase phase) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonPhase = objectMapper.writeValueAsString(phase); // convert phase into JSON string

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPhase)) // send JSON string in request
                .uri(URI.create("http://localhost:8080/changePhase"))
                .header("Content-Type", "application/json")
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
    }

    public static void sendPlayersPulledCards(Command[][] cards) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonPhase = objectMapper.writeValueAsString(cards); // convert phase into JSON string

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonPhase)) // send JSON string in request
                .uri(URI.create("http://localhost:8080/sendPlayersPulledCards"))
                .header("Content-Type", "application/json")
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
    }

    public static void sendOverPickedCards(Command[] playerPicked) throws Exception {
        int playerNumber = playerInfo.getPlayerId();

        ObjectMapper objectMapper = new ObjectMapper();

        SendCurrentCards info = new SendCurrentCards();
        info.setPlayerNumber(playerNumber);
        info.setPickedCards(playerPicked);

        String jsonInfo = objectMapper.writeValueAsString(info);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonInfo))
                .uri(URI.create("http://localhost:8080/sendOverPickedCards"))
                .header("Content-Type", "application/json")
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
    }

    public static String resetReadyList() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/resetReadyList"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return "something";

    }

    public static String resetUpgradeList() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/resetUpgradeList"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return "something";

    }

    public static String readyToReset() throws Exception {
        int playerNumber = playerInfo.getPlayerId();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/readyToReset/" + playerNumber))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return "something";
    }

    public static String readyReady() throws Exception {
        int playerNumber = playerInfo.getPlayerId();

        System.out.println(playerNumber + " is ready");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/readyReady/" + playerNumber))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return "something";
    }


    public static void sendPosition(int playerNumber, int x, int y, Heading heading, int currUpgrade) throws Exception {
        Map<String, Object> values = new HashMap<String, Object>() {{
            put("playerNumber", playerNumber);
            put("x", x);
            put("y", y);
            put("heading", heading.name()); // .name() method returns the name of this enum constant, as contained in the declaration
            put("currUpgrade", currUpgrade);
        }};

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/sendPosition"))
                .header("Content-Type", "application/json")
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);


    }

    public static int currentUpgradePlayer() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/currentUpgradePlayer"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        int result = Integer.valueOf(response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS));
        return result;
    }

    public static boolean setCurrentUpgradePlayer(int player) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/setCurrentUpgradePlayer?numberOfPlayers=" + player))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result.equals("something");
    }

    public static String removeCurrProgram() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/removeCurrProgram"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return "something";
    }

    public static boolean isLoadedGame() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/isLoadedGame"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        boolean result = Boolean.valueOf(response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS));
        return result;

    }

    public static int getCurrLoaded() throws Exception{

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/getCurrLoaded"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        int result = Integer.valueOf(response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS));
        return result;

    }

    public static void setGameState(Load load) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(load);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/setGameState"))
                .header("Content-Type", "application/json")
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
    }

    public static boolean setCheckpointForPlayer(int playerNumber) throws Exception{

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(Integer.toString(playerNumber)))
                .uri(URI.create("http://localhost:8080/setCheckpointsForPlayer"))
                .header("Content-Type", "text/plain;charset=UTF-8")
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return Boolean.parseBoolean(result);
    }

    public static boolean setPlayerHeadingInteractive(Heading heading, int playerNumber) throws Exception{
        Map<String, Object> data = new HashMap<>();
        data.put("heading", heading);
        data.put("playerNumber", playerNumber);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/setPlayerHeadingInteractive"))
                .header("Content-Type", "application/json")
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result.equals("bob");
    }


    public static boolean isPlayerChoice() throws Exception{

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/isPlayerChoice"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        boolean result = Boolean.valueOf(response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS));
        return result;

    }

    public static Command getPlayerCommandInteractive() throws Exception{

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/getPlayerCommandInteractive"))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

        ObjectMapper objectMapper = new ObjectMapper();
        Command command = objectMapper.readValue(result, Command.class);

        return command;
    }


}
