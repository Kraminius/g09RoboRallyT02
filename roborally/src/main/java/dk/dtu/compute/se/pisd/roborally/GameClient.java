package dk.dtu.compute.se.pisd.roborally;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally.model.GameLobby;

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


    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private static final int POLLING_INTERVAL_SECONDS = 5;

    private static ScheduledFuture<?> playerNamesPollingTask;

    public static void startPlayerNamesPolling() {

        playerNamesPollingTask = executorService.scheduleAtFixedRate(GameClient::pollPlayerNames, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public static void startCheckAllConnected(){

        executorService.scheduleWithFixedDelay(GameClient::pollPlayerNames, 0, POLLING_INTERVAL_SECONDS, TimeUnit.SECONDS);

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
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that occur during polling
        }
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


}
