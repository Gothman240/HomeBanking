package com.mindhub.HomeBanking;

import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class homebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(homebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository){
		return args -> {
			Client client = new Client("Melba","Morel","melba@mindhub.com");

			clientRepository.save(client);
		};
	}

}
