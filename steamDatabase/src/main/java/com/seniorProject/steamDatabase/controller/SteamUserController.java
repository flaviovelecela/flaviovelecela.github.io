package com.seniorProject.steamDatabase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.seniorProject.steamDatabase.model.GameInfo;
import com.seniorProject.steamDatabase.model.SteamUser;
import com.seniorProject.steamDatabase.service.SteamUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class SteamUserController {

    private final SteamUserService steamUserService;

    public SteamUserController(SteamUserService steamUserService) {
        this.steamUserService = steamUserService;
    }

    @GetMapping(value ="/createUser/{steamId}")
    ResponseEntity<SteamUser> createUser(@PathVariable String steamId) throws IOException, SteamApiException {
        return steamUserService.createUser(steamId);
    }

    @GetMapping(value = "/getGames/{steamId}")
    List<GameInfo> getSteamUserGames(@PathVariable String steamId) throws SteamApiException, IOException {
        return steamUserService.getOwnedGamesRequest(steamId);
    }

    @GetMapping(value = "/getAchievements/{steamId}/{appId}")
    int getPlayerAchievements (@PathVariable String steamId, @PathVariable int appId) {

        return steamUserService.achievementsRequest(appId, steamId);
    }

}
