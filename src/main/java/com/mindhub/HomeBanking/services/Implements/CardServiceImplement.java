package com.mindhub.HomeBanking.services.Implements;

import com.mindhub.HomeBanking.dtos.CardActivationDTO;
import com.mindhub.HomeBanking.models.Card;
import com.mindhub.HomeBanking.models.CardColor;
import com.mindhub.HomeBanking.models.CardType;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.repositories.CardRepository;
import com.mindhub.HomeBanking.services.CardService;
import com.mindhub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mindhub.HomeBanking.utils.CardUtils.getCardNumber;
import static com.mindhub.HomeBanking.utils.CardUtils.getCvv;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientService clientService;


    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public List<Card> findByClientAndTypeAndColorAndIsActive(Client client, CardType type, CardColor color, boolean isActive) {
        return cardRepository.findByClientAndTypeAndColorAndIsActive(client, type, color, isActive);
    }

    @Override
    public void updateIsActiveById(long id, boolean isActive) {
        Card card = cardRepository.findById(id).orElse(null);
        if (card != null){
            card.setActive(isActive);
            cardRepository.save(card);
        }else {
            throw new RuntimeException("Card not found");
        }

    }

    @Override
    public List<Card> getCardActive(Client client) {
        return cardRepository.findByClientAndIsActiveTrue(client);
    }
    @Override
    public ResponseEntity<Object> createNewCard(CardType type, CardColor color, Authentication auth) {
        Client client =  clientService.findByEmail(auth.getName());
        int cvv = getCvv();
        String cardNumber = getCardNumber();

        if (this.findByNumber(cardNumber) != null){
            return new ResponseEntity<>("Error with number card", HttpStatus.FORBIDDEN);
        }

        List<Card> cardList = this.findByClientAndTypeAndColorAndIsActive(client, type, color, true);

        if (cardList.size() <= 3){
            boolean hasCardColor = cardList.stream().anyMatch(card -> card.getColor().equals(color));
            if (hasCardColor){
                return new ResponseEntity<>("You already have a " + color + " " + type + " card.", HttpStatus.FORBIDDEN);
            }

            Card card = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cardNumber, cvv, true);
            client.addCards(card);
            this.save(card);
            return new ResponseEntity<>("A new " + type + " card has been created.", HttpStatus.CREATED);

        }else{
            return new ResponseEntity<>("You have reached the credit card limit.", HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<Object> switchBooleanIsActive(Long id, CardActivationDTO cardActivationDTO) {
        try{
            this.updateIsActiveById(id, cardActivationDTO.isActive());
            return new ResponseEntity<>("updated", HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
