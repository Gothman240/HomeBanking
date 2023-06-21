package com.mindhub.HomeBanking;

import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class homebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(homebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {
			Client melba = new Client("Melba","Morel","melba@mindhub.com");
			Account account = new Account("VIN001", LocalDate.now(), 5000.0);
			Account account1 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.0);

			clientRepository.save(melba);
			melba.addAccount(account);
			melba.addAccount(account1);

			accountRepository.save(account);
			accountRepository.save(account1);

			Transaction transaction1 = new Transaction(1000.00, "para Marco", LocalDateTime.now(), TransactionType.DEBIT);
			Transaction transaction2 = new Transaction(500.00, "Milk", LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction3 = new Transaction(100.00, "Other", LocalDateTime.now(), TransactionType.DEBIT);
			Transaction transaction4 = new Transaction(2000.00, "Other", LocalDateTime.now(), TransactionType.DEBIT);
			Transaction transaction5 = new Transaction(10.00, "Shop", LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction6 = new Transaction(50.00, "Bootcamp", LocalDateTime.now(), TransactionType.DEBIT);
			Transaction transaction7 = new Transaction(50.00, "7", LocalDateTime.now(), TransactionType.DEBIT);

			account.addTransactions(transaction1);
			account.addTransactions(transaction2);
			account.addTransactions(transaction3);
			account.addTransactions(transaction4);
			account.addTransactions(transaction5);
			account.addTransactions(transaction6);
			account.addTransactions(transaction7);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);

			Loan mortgage = new Loan("Mortgage", 500000.0, List.of(12,24,36,48,60));
			Loan personal = new Loan("Personal", 100000.0, List.of(6,12,24));
			Loan automotive = new Loan("Automotive", 300000.0, List.of(6,12,24,36));

			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);

			ClientLoan clientLoan = new ClientLoan(400000.0, 60);
			ClientLoan clientLoan1 = new ClientLoan(50000.0, 12);

			mortgage.addClientLoan(clientLoan);
			personal.addClientLoan(clientLoan1);
			melba.addClientLoan(clientLoan);
			melba.addClientLoan(clientLoan1);

			clientLoanRepository.save(clientLoan);
			clientLoanRepository.save(clientLoan1);

			Card cardMelbaGold = new Card(melba.getFirstName() + " " + melba.getLastName(), CardType.DEBIT,CardColor.GOLD, "1325-4567-7897", 129, LocalDate.now().plusYears(5), LocalDate.now());
			Card cardMelbaTitanium = new Card(melba.getFirstName() + " " + melba.getLastName(), CardType.CREDIT,CardColor.TITANIUM, "1330-4007-1897", 128, LocalDate.now().plusYears(5), LocalDate.now());
			//Card cardSilver = new Card(melba, CardType.CREDIT,CardColor.TITANIUM, "1330-1897-4007", (short) 127, LocalDate.now(), LocalDate.now().plusYears(5));

			//cardRepository.save(cardSilver);
			melba.addCards(cardMelbaGold);
			melba.addCards(cardMelbaTitanium);

			cardRepository.save(cardMelbaGold);
			cardRepository.save(cardMelbaTitanium);

		};
	}

}
