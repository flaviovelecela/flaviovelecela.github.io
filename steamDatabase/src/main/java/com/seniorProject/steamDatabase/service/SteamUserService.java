package com.seniorProject.steamDatabase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    private DecimalFormat df = new DecimalFormat("#.##");

    public ResponseEntity<SteamUser> createUser(String steamId) throws IOException, SteamApiException {
//        JsonNode jsonNode = mapper.readTree(jsonObject);
        // mapper part doesn't work yet
        SteamUser steamUser = new SteamUser();
        steamUser.setSteamId(steamId);
    //  user.setUserName(jsonNode.get("userName").asText());
        LOGGER.warn(String.valueOf(steamUser.getSteamId()));
        steamUser.setGameList(getOwnedGamesRequest(steamUser.getSteamId()));

        return ResponseEntity.ofNullable(steamUser);
    }

    public List<GameInfo> getOwnedGamesRequest(String userId) throws SteamApiException, IOException {

        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(API_KEY).build();
        GetOwnedGamesRequest request = new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(userId).includeAppInfo(true).buildRequest();
        GetOwnedGames getOwnedGames = client.processRequest(request);
        LOGGER.warn(String.valueOf(getOwnedGames));

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String ownedGamesJson = ow.writeValueAsString(getOwnedGames);
        JsonNode jsonNode = mapper.readTree(ownedGamesJson).get("response").get("games");

        return mapToObject(jsonNode, userId);

    }

    public int GetPlayerAchievementsRequest(int appId, String userId) throws SteamApiException, JsonProcessingException {
        int achievedNum;
        int achieved = 0;
        int size = 0;

        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(API_KEY).build();
        GetPlayerAchievementsRequest request = new GetPlayerAchievementsRequest.GetPlayerAchievementsRequestBuilder(userId, appId).buildRequest();
        GetPlayerAchievements getPlayerAchievements = client.processRequest(request);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String achievementsJson = ow.writeValueAsString(getPlayerAchievements);
        JsonNode achievementsJsons = mapper.readTree(achievementsJson).get("playerstats").get("achievements");

        if (achievementsJsons.isArray()) {
            for (JsonNode jsonNode : achievementsJsons) {
                size++;
                achievedNum = jsonNode.get("achieved").asInt();
                if (achievedNum == 1) {
                    achieved++;
                }
            }
        }

        return achieved * 100 / size;
    }

    private List<GameInfo> mapToObject(JsonNode ownedGamesList, String userId) throws SteamApiException, JsonProcessingException {
        LOGGER.info("MAPPING: ");

        List<GameInfo> gameInfoList = new ArrayList<>();

        if (ownedGamesList.isArray()) {
            for (JsonNode jsonNode : ownedGamesList) {
                GameInfo gameInfo = new GameInfo();
                gameInfo.setAppId(jsonNode.get("appid").asInt());
                gameInfo.setName(jsonNode.get("name").asText());
                gameInfo.setTotalPlaytime(Double.parseDouble(df.format(jsonNode.get("playtime_forever").asInt()/60.0)));
                gameInfo.setImageIcon(jsonNode.get("img_icon_url").asText());
                gameInfo.setRating("N/A");
                gameInfoList.add(gameInfo);
            }
        }
        steamGameRepository.saveAll(gameInfoList);
        return gameInfoList;
    }

    public int achievementsRequest(int appId, String steamId) {
        try {
            return GetPlayerAchievementsRequest(appId, steamId);
        }
        catch (SteamApiException | ArithmeticException | JsonProcessingException e) {
            return -1;
        }
    }

    public double recentlyPlayedGames(String steamID) throws SteamApiException, JsonProcessingException {
        double playtime = 0;

        SteamWebApiClient client = new SteamWebApiClient.SteamWebApiClientBuilder(API_KEY).build();
        GetOwnedGamesRequest request = new GetOwnedGamesRequest.GetOwnedGamesRequestBuilder(steamID).includeAppInfo(true).buildRequest();
        GetOwnedGames getOwnedGames = client.processRequest(request);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String getRecentPlayGames = ow.writeValueAsString(getOwnedGames);
        JsonNode playtimeJson = mapper.readTree(getRecentPlayGames).get("response").get("games");

        if (playtimeJson.isArray()) {
            for (JsonNode jsonNode : playtimeJson) {
                if (!(jsonNode.get("playtime_2weeks") == null)) {
                    playtime += jsonNode.get("playtime_2weeks").asInt();
                }
            }
        }
        return Double.parseDouble(df.format(playtime/60.0));
    }
}

