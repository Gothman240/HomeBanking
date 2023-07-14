package com.mindhub.HomeBanking.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

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

        boolean hasDebit = client.getCards().stream().filter(card -> card.getType() == CardType.DEBIT).count() <= 3;
        boolean hasCredit = client.getCards().stream().filter(card -> card.getType() == CardType.CREDIT).count() <= 3;
        boolean hasColorCredit = client.getCards().stream().anyMatch(card -> card.getColor() == color &&  card.getType().equals(CardType.CREDIT));
        boolean hasColorDebit = client.getCards().stream().anyMatch(card -> card.getColor() == color && card.getType().equals(CardType.DEBIT));

        if (type.equals(CardType.CREDIT)){
            if (hasCredit) {
                if (hasColorCredit) {
                    return new ResponseEntity<>("You already have a " + color + " credit card.", HttpStatus.FORBIDDEN);
                } else {
                    Card card = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cardNumber, cvv);
                    client.addCards(card);
                    cardService.save(card);
                    return new ResponseEntity<>("A new credit card has been created.", HttpStatus.CREATED);
                }
            }else{
                return new ResponseEntity<>("You have reached the credit card limit.", HttpStatus.FORBIDDEN);
            }

        } else if (type.equals(CardType.DEBIT)) {
            if (hasDebit) {
                if (hasColorDebit) {
                    return new ResponseEntity<>("You already have a " + color + " debit card.", HttpStatus.FORBIDDEN);
                } else {
                    Card card = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cardNumber, cvv);
                    client.addCards(card);
                    cardService.save(card);
                    return new ResponseEntity<>("A new debit card has been created.", HttpStatus.CREATED);
                }
            }else{
                return new ResponseEntity<>("You have reached the credit card limit.", HttpStatus.FORBIDDEN);
            }

        }else {
            return new ResponseEntity<>("You have reached the cards limit.", HttpStatus.FORBIDDEN);
        }

    }




}
