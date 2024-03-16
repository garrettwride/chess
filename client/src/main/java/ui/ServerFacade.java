package ui;

import com.google.gson.*;
import exception.ResponseException;
import model.*;
import server.SuccessResponse;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {

    //should have 7 methods for each end pint that called client communicator
    //login, logot, create user, create game, list games, join games, observe games

    private final String serverUrl;
    public Gson gson = new Gson();

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public GameInfo addGame(GameInfo game, String auth) throws ResponseException {
        var path = "/game";
        Map<String, String> headers = new HashMap<>();
        if (auth != null) {
            headers.put("Authorization", auth);
        }
        return this.makeRequest("POST", path, game, headers, GameInfo.class);
    }

    public void deleteGame(int id, String auth) throws ResponseException {
        var path = String.format("/game/%s", id);
        Map<String, String> headers = new HashMap<>();
        if (auth != null) {
            headers.put("Authorization", auth);
        }
        this.makeRequest("DELETE", path, null, headers, null);
    }

    public AuthData authenticate(UserData userData) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, userData, null, AuthData.class);
    }


    public void deauthenticate(String authToken) throws ResponseException {
        var path = "/session";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authToken);
        this.makeRequest("DELETE", path, null, headers, SuccessResponse.class);
    }

    public void deleteAllGames(String auth) throws ResponseException {
        var path = "/game";
        Map<String, String> headers = new HashMap<>();
        if (auth != null) {
            headers.put("Authorization", auth);
        }
        this.makeRequest("DELETE", path, null, headers, null);
    }

    public void joinGame(GameInfo gameInfo, String authToken) throws ResponseException {
        var path = "/game";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authToken);

        // Convert the GameInfo object to JSON
        String requestBody = gson.toJson(gameInfo);

        // Make the request to join the game
        makeRequest("PUT", path, requestBody, headers, String.class);
    }
    public JsonArray listGames(String auth) throws ResponseException {
        var path = "/game";
        Map<String, String> headers = new HashMap<>();
        if (auth != null) {
            headers.put("Authorization", auth);
        }

        JsonObject jsonResponse = this.makeRequest("GET", path, null, headers, JsonObject.class);

        // Extract the JSON array containing the game objects
        JsonArray gamesArray = jsonResponse.getAsJsonArray("games");

        return gamesArray;

    }

    public AuthData register(UserData userData) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, userData, null, AuthData.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Map<String, String> headers, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeHeaders(headers, http);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private static void writeHeaders(Map<String, String> headers, HttpURLConnection http) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}