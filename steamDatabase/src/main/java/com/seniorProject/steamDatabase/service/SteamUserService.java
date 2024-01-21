package com.seniorProject.steamDatabase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.seniorProject.steamDatabase.model.SteamUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@RequiredArgsConstructor
public class SteamUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SteamUserService.class);
    private final ObjectMapper mapper;
    @Value("${steam.api-key")
    private String API_KEY;

    public ResponseEntity<SteamUser> createUser(String jsonObject) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(jsonObject);
        SteamUser user = new SteamUser();
        user.setSteamId(jsonNode.get("steamId").asInt());
        user.setUserName(jsonNode.get("userName").asText());
    //  user.setGameList(jsonNode.get("gameList"));

        return ResponseEntity.ofNullable(user);
    }

    public static GetOwnedGames GetOwnedGamesRequest(String userId) throws SteamApiException {
        // need to find some way to get API_KEY value to work here instead of hard coding it
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder("94A07FD5B1267700D27D6D2B3CF9583C").build();
        GetOwnedGamesRequest request = new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(userId).includeAppInfo(true).buildRequest();
        GetOwnedGames getOwnedGames = client.<GetOwnedGames> processRequest(request);

        return getOwnedGames;
        // Add same thing for getPlayerAchievements
    }
}
