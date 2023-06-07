package com.mindhub.HomeBanking;

import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class homebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(homebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return args -> {
			Client melba = new Client("Melba","Morel","melba@mindhub.com");
			Account account = new Account("VIN001", LocalDate.now(), 5000.0);
			Account account1 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.0);
			clientRepository.save(melba);
			melba.addAccount(account);
			melba.addAccount(account1);
			accountRepository.save(account);
			accountRepository.save(account1);
		};
	}

}
