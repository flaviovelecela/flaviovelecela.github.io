package com.seniorProject.steamDatabase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.playerachievements.GetPlayerAchievements;
import com.lukaspradel.steamapi.data.json.playerstats.GetUserStatsForGame;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.GetPlayerAchievementsRequest;
import com.lukaspradel.steamapi.webapi.request.GetUserStatsForGameRequest;
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
    @Value("${steam.api-key}")
    private String API_KEY;
    private SteamUser steamUser;

    public ResponseEntity<SteamUser> createUser(String jsonObject) throws JsonProcessingException {
//        JsonNode jsonNode = mapper.readTree(jsonObject);
        // mapper part doesn't work yet
        steamUser = new SteamUser();
        steamUser.setSteamId(jsonObject);
    //  user.setUserName(jsonNode.get("userName").asText());
    //  user.setGameList(jsonNode.get("gameList"));
        LOGGER.warn(steamUser.getSteamId());

        return ResponseEntity.ofNullable(steamUser);
    }

    public GetOwnedGames GetOwnedGamesRequest(String userId) throws SteamApiException {
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(API_KEY).build();
        GetOwnedGamesRequest request = new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(userId).includeAppInfo(true).buildRequest();
        GetOwnedGames getOwnedGames = client.<GetOwnedGames> processRequest(request);

        return getOwnedGames;
    }

    public GetPlayerAchievements GetPlayerAchievementsRequest(Integer appId) throws SteamApiException {
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(API_KEY).build();
        GetPlayerAchievementsRequest request = new GetPlayerAchievementsRequest.GetPlayerAchievementsRequestBuilder(steamUser.getSteamId(), appId).buildRequest();
        GetPlayerAchievements getPlayerAchievements = client.<GetPlayerAchievements> processRequest(request);

        return getPlayerAchievements;
    }

//    public static GetUserStatsForGame GetUserStatsForGameRequest(int appId) throws SteamApiException {
//        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(API_KEY).build();
//        GetUserStatsForGameRequest request = new GetUserStatsForGameRequest.GetUserStatsForGameRequestBuilder(steamUser.getSteamId(), appId).buildRequest();
//        GetUserStatsForGame getUserStatsForGame = client.<GetUserStatsForGame> processRequest(request);
//
//        return getUserStatsForGame;
//    }
}
