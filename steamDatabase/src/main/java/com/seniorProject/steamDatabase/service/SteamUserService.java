package com.seniorProject.steamDatabase.service;

import com.fasterxml.jackson.core.JsonParser;
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
import com.seniorProject.steamDatabase.repository.SteamGameRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Service
@Component
@RequiredArgsConstructor
public class SteamUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SteamUserService.class);
    private final SteamGameRepository steamGameRepository;
    private final ObjectMapper mapper;
    @Value("${steam.api-key}")
    private String API_KEY;
    private SteamUser steamUser;
    private DecimalFormat df = new DecimalFormat("#.##");

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

    public GetOwnedGames GetOwnedGamesRequest(String userId) throws SteamApiException, IOException {

        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(API_KEY).build();
        GetOwnedGamesRequest request = new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(userId).includeAppInfo(true).buildRequest();
        GetOwnedGames getOwnedGames = client.processRequest(request);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String ownedGamesJson = ow.writeValueAsString(getOwnedGames);
        JsonNode jsonNode = mapper.readTree(ownedGamesJson).get("response").get("games");

        mapToObject(jsonNode);
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

    private void mapToObject(JsonNode ownedGamesList) throws SteamApiException {
        LOGGER.info("MAPPING: ");

        List<GameInfo> gameInfoList = new ArrayList<>();

        if (ownedGamesList.isArray()) {
            for (JsonNode jsonNode : ownedGamesList) {
                GameInfo gameInfo = new GameInfo();
                gameInfo.setAppId(jsonNode.get("appid").asInt());
                gameInfo.setName(jsonNode.get("name").asText());
                gameInfo.setTotalPlaytime(Double.parseDouble(df.format(jsonNode.get("playtime_forever").asInt()/60.0)));
//                gameInfo.setAchievements(GetPlayerAchievementsRequest(gameInfo.getAppId()));
                gameInfo.setImageIcon(jsonNode.get("img_icon_url").asText());
                LOGGER.warn(String.valueOf(gameInfo));
                gameInfoList.add(gameInfo);
            }
        }
        steamGameRepository.saveAll(gameInfoList);
    }
}
