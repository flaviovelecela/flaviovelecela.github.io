package com.seniorProject.steamDatabase.controller;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.seniorProject.steamDatabase.service.SteamUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SteamUserController {

    private final SteamUserService steamUserService;

    public SteamUserController(SteamUserService steamUserService) {
        this.steamUserService = steamUserService;
    }

    @GetMapping(value = "/getGames")
    public GetOwnedGames getSteamUserGames(@RequestBody String userId) throws SteamApiException {
        return SteamUserService.GetOwnedGamesRequest(userId);
    }
}
