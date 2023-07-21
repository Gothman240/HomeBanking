package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.ClientDTO;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import com.mindhub.HomeBanking.services.AccountService;
import com.mindhub.HomeBanking.services.ClientService;
import com.mindhub.HomeBanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;
    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.findAll();
    }

    @RequestMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        String random;

        do{
            random = CardUtils.getRandomAccountNumber();

        }while (accountService.existsByNumber(random));

        if (firstName.isBlank()) {
            return new ResponseEntity<>("Missing firstName", HttpStatus.FORBIDDEN);
        }

        if (lastName.isBlank()) {
            return new ResponseEntity<>("Missing lastName", HttpStatus.FORBIDDEN);
        }

        if (email.isBlank()) {
            return new ResponseEntity<>("Missing email", HttpStatus.FORBIDDEN);
        }

        if (password.isBlank()) {
            return new ResponseEntity<>("Missing password", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }



        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = new Account(random, true);

        client.addAccount(account);

        clientService.save(client);
        accountService.save(account);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping("/clients/current")
    public ClientDTO getAuthClient(Authentication auth){
        return new ClientDTO(clientService.findByEmail(auth.getName()));
    }
}
