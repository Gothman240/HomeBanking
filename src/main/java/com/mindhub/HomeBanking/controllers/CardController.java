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
    private ClientService clientService;
    @Autowired
    private CardService cardService;
    @RequestMapping(value = "/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(
            @RequestParam CardType type, @RequestParam CardColor color, Authentication auth){
        Client client =  clientService.findByEmail(auth.getName());
        int cvv = getCvv();
        String cardNumber = getCardNumber();

        if (cardService.findByNumber(cardNumber) != null){
            return new ResponseEntity<>("Error with number card",HttpStatus.FORBIDDEN);
        }

        List<Card> cardList = cardService.findByClientAndTypeAndColorAndIsActive(client, type, color, true);

        if (cardList.size() <= 3){
            boolean hasCardColor = cardList.stream().anyMatch(card -> card.getColor().equals(color));
            if (hasCardColor){
                return new ResponseEntity<>("You already have a " + color + " " + type + " card.", HttpStatus.FORBIDDEN);
            }

            Card card = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cardNumber, cvv, true);
            client.addCards(card);
            cardService.save(card);
            return new ResponseEntity<>("A new " + type + " card has been created.", HttpStatus.CREATED);

        }else{
            return new ResponseEntity<>("You have reached the credit card limit.", HttpStatus.FORBIDDEN);
        }


    }

    @RequestMapping(value = "/active/cards")
    public List<CardDTO> getActiveCards(){
        //current
        return cardService.getCardActive().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/cards/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> deleteCard(@PathVariable Long id, @RequestBody CardActivationDTO cardActivationDTO){
        try{
            cardService.updateIsActiveById(id, cardActivationDTO.isActive());
            return new ResponseEntity<>("updated", HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }




}