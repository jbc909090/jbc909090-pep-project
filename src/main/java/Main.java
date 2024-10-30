import java.net.http.HttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import Controller.SocialMediaController;
import Util.ConnectionUtil;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        ConnectionUtil.resetTestDatabase();
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        HttpClient webClient = HttpClient.newHttpClient();
        app.stop();
        app.start(8080);
        System.out.println("start");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/1"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{"+
                        "\"message_text\": \"updated message\" }"))
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse response2 = webClient.send(request2, HttpResponse.BodyHandlers.ofString());
            int status2 = response2.statusCode();
            String response = response2.body().toString();
            System.out.println(response);
            System.out.println(status2);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
