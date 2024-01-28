package com.seniorProject.steamDatabase.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SteamUser {

    private String steamId;
    private String userName;
    private List<GameInfo> gameList;
}

