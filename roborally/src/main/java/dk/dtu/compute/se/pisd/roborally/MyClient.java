package dk.dtu.compute.se.pisd.roborally;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Converter;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
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
     * @author Nicklas Christensen     s224314.dtu.dk
     * @param load the current version of the game being send to the server
     * @return whether the server succeded at getting the game or not
     * @throws Exception
     */
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

    /**
     * @author Nicklas Christensen      s224314.dtu.dk
     * @param commands the programCards of the player we are sending
     * @param player the player number of the player who's cards we are sending
     * @return wether we succeded at sending our cards or not
     */
    public static boolean sendProgramCards(Command[] commands, int player) throws Exception {
        String productJSON = new Gson().toJson(commands);
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(productJSON))
                .uri(URI.create("http://localhost:8080/updateProgramCards/" + player))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        return result.equals("acknowledged");
    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * A method used to call the server for the current gameState
     * @return a Load that can be loaded into the game to
     */
    public static Phase getPhase() {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/phase"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            Gson gson = new Gson();
            Phase phase = gson.fromJson(result, Phase.class);
            return phase;
        } catch (Exception e) {
            return null;
        }
    }


}
