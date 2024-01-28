package com.seniorProject.steamDatabase;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SteamDatabaseApplication implements ApplicationRunner {
	// model class contains important data like steam id and game information (SteamUser contains list of games) (Games class contains game info)
	//

	public static void main(String[] args) {
		SpringApplication.run(SteamDatabaseApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

	}

}
