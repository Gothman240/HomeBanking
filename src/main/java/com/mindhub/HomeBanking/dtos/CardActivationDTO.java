package com.mindhub.HomeBanking.dtos;

public class CardActivationDTO {
    private boolean isActive;

    public CardActivationDTO() {
    }

    public CardActivationDTO(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }
}
