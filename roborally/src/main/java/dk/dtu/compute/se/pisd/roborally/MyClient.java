package dk.dtu.compute.se.pisd.roborally;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MyClient {



    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();





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

    //server save and load
    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     */
    public static boolean setSave(String name) throws Exception {

        System.out.println("We are about to send to our server");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://localhost:8080/addSave/" + name))
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        System.out.println(result);
        return result.equals("gameSaved");
    }


    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     */
    public static String[] getSaveNames(){
        try{
            System.out.println("We are asking for saveGame names");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/getSaveNames"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            Gson gson = new Gson();
            String[] strings  = gson.fromJson(result, String[].class);
            return strings;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * @param saveName the name of the saved gameState we want
     * @return the Load of the gameState we asked for
     * @throws Exception
     */
    public static Load getSave(String saveName) {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/getSave/" + saveName))
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

}
