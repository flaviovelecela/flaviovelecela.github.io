package com.seniorProject.steamDatabase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import com.github.koraktor.steamcondenser.steam.community.WebApi;
import com.seniorProject.steamDatabase.model.SteamUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.github.koraktor.steamcondenser.*;

import java.util.Collections;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class SteamUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SteamUserService.class);
    private final ObjectMapper mapper;
    @Value("${steam.api-key}")
    private String API_KEY;

    public ResponseEntity<SteamUser> createUser(String jsonObject) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(jsonObject);
        SteamUser user = new SteamUser();
        user.setSteamId(jsonNode.get("steamId").asInt());
        user.setUserName(jsonNode.get("userName").asText());
//        user.setGameList(jsonNode.get("gameList"));

        return ResponseEntity.ofNullable(user);
    }

    public ResponseEntity<HashMap<Integer, SteamGame>> getSteamUser(long username) {
        try {
            WebApi.setApiKey(API_KEY);
            SteamId id = SteamId.create(username);
            return ResponseEntity.ofNullable(id.getGames());
        } catch (SteamCondenserException c) {
            c.getMessage();
        }
        return ResponseEntity.ofNullable(new HashMap<>());

    }

}
