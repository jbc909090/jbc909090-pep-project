package Controller;

import Model.Account;
import Model.Message;
import Service.ServiceSocialMedia;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    ServiceSocialMedia service;
    public SocialMediaController() {
        service = new ServiceSocialMedia();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    /*
     * End points
     * POST localhost:8080/register
     * POST localhost:8080/login
     * POST localhost:8080/messages
     * GET localhost:8080/messages
     * GET localhost:8080/messages/{message_id}
     * DELETE localhost:8080/messages/{message_id}
     * PATCH localhost:8080/messages/{message_id}
     * GET localhost:8080/accounts/{account_id}/messages
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::messagePoster);
        app.get("/messages", this::messageHandler);
        app.get("/messages/{message_id}", this::messageFinder);
        app.delete("/messages/{message_id}", this::messageRemover);
        app.patch("/messages/{message_id}", this::messageUpdater);
        app.get("/accounts/{account_id}/messages", this::userMessageHandler);
        return app;
    }
    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    /*
     * error codes and flags are needed, everything needed to be returned is returned as JSON
     * in order
     * 1. create new user
     * 2. login existing user
     * 3. create new message
     * 4. query for a specific singular message
     * 5. retrieve all messages
     * 6. delete a specific message
     * 7. modify a specific message
     * 8. query for all messages by one user
     */
    //instatiate the service class use its methods to complete whats needed
    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = service.addAccount(account);
        if (addedAccount == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedAccount));
        }
    }
    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loginAccount = service.login(account);
        if (loginAccount == null) {
            context.status(401);
        } else {
            context.json(mapper.writeValueAsString(loginAccount));
        }
    }
    private void messagePoster(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = service.addMessage(message);
        if (addedMessage == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedMessage));
        }
    }
    private void messageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        context.json(mapper.writeValueAsString(service.getAllMessages()));
    }
    private void messageFinder(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = service.getMessageByID(message_id);
        if (message == null) {
            context.json("");
        } else {
            context.json(mapper.writeValueAsString(message));
        }
    }
    private void messageRemover(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = service.deleteMessage(message_id);
        if (message == null) {
            context.json("");
        } else {
            context.json(mapper.writeValueAsString(message));
        }
    }
    private void messageUpdater(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        String message_text = context.body();//json formating is wrong
        message_text = message_text.substring(18, (message_text.length() - 3));
        Message message = service.updateMessage(message_id, message_text);
        if (message == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(message));
        }

    }
    private void userMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(mapper.writeValueAsString(service.getAllUserMessages(account_id)));
    }

}