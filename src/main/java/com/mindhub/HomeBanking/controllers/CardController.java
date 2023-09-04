package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.CardActivationDTO;
import com.mindhub.HomeBanking.dtos.CardDTO;
import com.mindhub.HomeBanking.models.Card;
import com.mindhub.HomeBanking.models.CardColor;
import com.mindhub.HomeBanking.models.CardType;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.services.CardService;
import com.mindhub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.mindhub.HomeBanking.utils.CardUtils.getCardNumber;
import static com.mindhub.HomeBanking.utils.CardUtils.getCvv;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;
    @RequestMapping(value = "/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(
            @RequestParam CardType type, @RequestParam CardColor color, Authentication auth){
        return cardService.createNewCard(type, color, auth);
    }

    @RequestMapping(value = "/active/cards")
    public List<CardDTO> getActiveCards(){
        //current
        return cardService.getCardActive().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/cards/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> deleteCard(@PathVariable Long id, @RequestBody CardActivationDTO cardActivationDTO){
        return cardService.switchBooleanIsActive(id, cardActivationDTO);
    }




}