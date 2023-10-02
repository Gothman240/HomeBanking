package com.mindhub.HomeBanking.repositories;

import com.mindhub.HomeBanking.models.Card;
import com.mindhub.HomeBanking.models.CardColor;
import com.mindhub.HomeBanking.models.CardType;
import com.mindhub.HomeBanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
     Card findByNumber(String number);
     List<Card> findByClientAndTypeAndColorAndIsActive(Client client, CardType type, CardColor color, boolean isActive);
     List<Card> findByClientAndIsActiveTrue(Client client);
}
