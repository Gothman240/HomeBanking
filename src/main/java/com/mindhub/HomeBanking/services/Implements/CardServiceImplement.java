package com.mindhub.HomeBanking.services.Implements;

import com.mindhub.HomeBanking.models.Card;
import com.mindhub.HomeBanking.models.CardColor;
import com.mindhub.HomeBanking.models.CardType;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.repositories.CardRepository;
import com.mindhub.HomeBanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;


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
    public List<Card> getCardActive() {
        return cardRepository.findByIsActiveTrue();
    }


}
