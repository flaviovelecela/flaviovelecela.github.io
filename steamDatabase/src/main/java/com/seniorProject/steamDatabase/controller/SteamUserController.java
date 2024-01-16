package com.seniorProject.steamDatabase.controller;

import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.seniorProject.steamDatabase.service.SteamUserService;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class SteamUserController {

    private final SteamUserService steamUserService;

    public SteamUserController(SteamUserService steamUserService) {
        this.steamUserService = steamUserService;
    }

    @GetMapping(value = "/getGames")
    ResponseEntity<HashMap<Integer, SteamGame>> getSteamUserInfo(@RequestBody long username) {
        return steamUserService.getSteamUser(username);
        // look into seeing how to get it to return as a json and not xml look in steam id class bitch
    }
}
