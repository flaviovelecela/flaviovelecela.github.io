package com.seniorProject.steamDatabase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SteamDatabaseApplication {
	// model class contains important data like steam id and game information (SteamUser contains list of games) (Games class contains game info)
	//

	public static void main(String[] args) {
		SpringApplication.run(SteamDatabaseApplication.class, args);
	}

}
