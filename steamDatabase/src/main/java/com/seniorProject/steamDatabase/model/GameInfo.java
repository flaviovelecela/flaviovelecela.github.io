package com.seniorProject.steamDatabase.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "GAME_INFO", schema = "STEAM")
public class GameInfo {
    @Id
    @Column(name = "APP_ID")
    private int appId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "TOTAL_PLAYTIME")
    private double totalPlaytime;
    @Column(name = "ACHIEVEMENTS")
    private String achievements;
    @Column(name = "IMAGE_ICON")
    private String imageIcon;
    @Column(name = "RATING")
    private String rating;
}
