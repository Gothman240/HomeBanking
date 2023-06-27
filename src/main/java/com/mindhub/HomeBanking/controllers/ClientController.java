package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.ClientDTO;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {


        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
    @RequestMapping("/clients/current")
    public ClientDTO getAuthClient(Authentication auth){
        return new ClientDTO(clientRepository.findByEmail(auth.getName()));
    }
}
