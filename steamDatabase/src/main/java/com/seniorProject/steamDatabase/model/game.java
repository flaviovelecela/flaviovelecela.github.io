package com.seniorProject.steamDatabase.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class game {
    private int appId;
    private String name;
    private int recentPlaytime;
    private int totalPlaytime;

}
