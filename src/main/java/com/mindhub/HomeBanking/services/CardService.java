package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.models.Card;

public interface CardService {
    Card findByNumber(String number);
    Card save(Card card);
}
