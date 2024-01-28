package com.seniorProject.steamDatabase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.playerachievements.GetPlayerAchievements;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.GetPlayerAchievementsRequest;
import com.seniorProject.steamDatabase.model.GameInfo;
import com.seniorProject.steamDatabase.model.SteamUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Component
@RequiredArgsConstructor
public class SteamUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SteamUserService.class);
    private final ObjectMapper mapper;
    @Value("${steam.api-key}")
    private String API_KEY;
    private SteamUser steamUser;
    private GameInfo gameInfo;

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

    public GetOwnedGames GetOwnedGamesRequest(String userId) throws SteamApiException, JsonProcessingException {
        TypeReference<List<GetOwnedGames>> listType = new TypeReference<>() {};

        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(API_KEY).build();
        GetOwnedGamesRequest request = new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(userId).includeAppInfo(true).buildRequest();
        GetOwnedGames getOwnedGames = client.<GetOwnedGames>processRequest(request);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String ownedGamesJson = ow.writeValueAsString(getOwnedGames);
        List<GetOwnedGames> getOwnedGamesList = mapper.readValue(ownedGamesJson, listType);

        mapToObject(getOwnedGamesList);
        return getOwnedGames;

    }

    public String GetPlayerAchievementsRequest(Integer appId) throws SteamApiException {
        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(API_KEY).build();
        GetPlayerAchievementsRequest request = new GetPlayerAchievementsRequest.GetPlayerAchievementsRequestBuilder(steamUser.getSteamId(), appId).buildRequest();
        GetPlayerAchievements getPlayerAchievements = client.<GetPlayerAchievements> processRequest(request);

        return getPlayerAchievements.toString();
    }

//    public static GetUserStatsForGame GetUserStatsForGameRequest(int appId) throws SteamApiException {
//        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(API_KEY).build();
//        GetUserStatsForGameRequest request = new GetUserStatsForGameRequest.GetUserStatsForGameRequestBuilder(steamUser.getSteamId(), appId).buildRequest();
//        GetUserStatsForGame getUserStatsForGame = client.<GetUserStatsForGame> processRequest(request);
//
//        return getUserStatsForGame;
//    }

    private void mapToObject(List<GetOwnedGames> ownedGamesList) throws JsonProcessingException, SteamApiException {
        LOGGER.info("MAPPING: ");

        List<GameInfo> gameInfoList = new ArrayList<>();
        int gameNum = 0;

        for (GetOwnedGames getOwnedGames : ownedGamesList) {
            GameInfo gameInfo = new GameInfo();
            JsonNode jsonNode = mapper.readTree(String.valueOf(getOwnedGames));
            gameInfo.setAppId(jsonNode.get("response").get("games").get(gameNum).get("appid").asInt());
            gameInfo.setName(jsonNode.get("response").get("games").get(gameNum).get("name").asText());
            gameInfo.setTotalPlaytime(jsonNode.get("response").get("games").get(gameNum).get("playtime_forever").asInt());
            gameInfo.setAchievements(GetPlayerAchievementsRequest(gameInfo.getAppId()));
            gameInfo.setImageIcon(jsonNode.get("response").get("games").get(gameNum).get("img_icon_url").asText());
            gameInfoList.add(gameInfo);
            gameNum+=1;
        }
    }
}
