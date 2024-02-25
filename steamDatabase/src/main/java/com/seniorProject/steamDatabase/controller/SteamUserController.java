package com.seniorProject.steamDatabase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.playerachievements.GetPlayerAchievements;
import com.lukaspradel.steamapi.data.json.playerstats.GetUserStatsForGame;
import com.seniorProject.steamDatabase.model.SteamUser;
import com.seniorProject.steamDatabase.service.SteamUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SteamUserController {

    private final SteamUserService steamUserService;

    public SteamUserController(SteamUserService steamUserService) {
        this.steamUserService = steamUserService;
    }

    @GetMapping(value ="/createUser")
    ResponseEntity<SteamUser> createUser(@RequestBody String steamId) throws IOException, SteamApiException {
        return steamUserService.createUser(steamId);
    }

    @GetMapping(value = "/getGames")
    GetOwnedGames getSteamUserGames(@RequestBody String userId) throws SteamApiException, IOException {
        return steamUserService.GetOwnedGamesRequest(userId);
    }

//    @GetMapping(value = "/getAchievements")
//    GetPlayerAchievements getPlayerAchievements (@RequestBody Integer appId) throws SteamApiException {
//
//        return steamUserService.GetPlayerAchievementsRequest(appId);
//    }

}
