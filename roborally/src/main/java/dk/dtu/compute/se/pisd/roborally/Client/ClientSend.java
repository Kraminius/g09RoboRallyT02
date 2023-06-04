package dk.dtu.compute.se.pisd.roborally.Client;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ClientSend {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    //This may need to change so it's not always on ones own computer
    public static final String ServerURI = "http://localhost:8080";

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * This method is meant to send all CommandCardFields to a singular player
      * @param commandCardField all players CommandCardFields
     * @param id the player we are sending it to
     * @return
     */

    public static boolean sendOwnProgram(ArrayList<CommandCardField> commandCardField, int id) {
        try{
            //Might need another way to make JSONObjects
            String productJSON = new Gson().toJson(commandCardField);
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(productJSON))
                    .uri(URI.create(ServerURI + "/playerProgram/"+ id))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("acknowledged")? true : false;
        } catch (Exception e) {
            return false;
        }
    }

    //This may be the wrong way to do it
    public static String getActivate() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/activate"))
                .setHeader("User-Agent", "Product Client")
                .header("Content-Type", "application/json")
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
        if(result == "proceed"){
            ClientProgramService.goActivate = true;
        }
        return result;
    }

}
