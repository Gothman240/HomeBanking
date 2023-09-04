package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.dtos.CardActivationDTO;
import com.mindhub.HomeBanking.models.Card;
import com.mindhub.HomeBanking.models.CardColor;
import com.mindhub.HomeBanking.models.CardType;
import com.mindhub.HomeBanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {
    Card findByNumber(String number);
    Card save(Card card);
    List<Card> findByClientAndTypeAndColorAndIsActive(Client client, CardType type, CardColor color, boolean isActive);
    void updateIsActiveById(long id, boolean isActive);
    List<Card> getCardActive();

    ResponseEntity<Object> createNewCard(CardType type, CardColor color, Authentication auth);

    ResponseEntity<Object> switchBooleanIsActive(Long id, CardActivationDTO cardActivationDTO);

}
