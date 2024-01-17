package com.seniorProject.steamDatabase.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SteamUser {

    private int steamId;
    private String userName;
    private List<game> gameList;
}

