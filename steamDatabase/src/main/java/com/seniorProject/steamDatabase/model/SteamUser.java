package com.seniorProject.steamDatabase.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Entity
@Table(name = "USER_INFO", schema = "STEAM")
public class SteamUser {
    @Id
    @Column(name = "STEAM_ID")
    private String steamId;
    @Column(name = "USERNAME")
    private String username;

}

