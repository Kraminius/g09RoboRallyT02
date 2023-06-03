package dk.dtu.compute.se.pisd.roborally.Server;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ServerSend {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * This method is meant to send all CommandCardFields to a singular player
      * @param commandCardField all players CommandCardFields
     * @param id the player we are sending it to
     * @return
     */

    public boolean sendProgram(CommandCardField commandCardField, int id) {
        try{
            //Might need another way to make JSONObjects
            String productJSON = new Gson().toJson(commandCardField);
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(productJSON))
                    .uri(URI.create("http://localhost:8080/Player/"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("added")? true : false;
        } catch (Exception e) {
            return false;
        }
    }

}
