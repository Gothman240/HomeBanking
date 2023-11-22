package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.dtos.ClientDTO;
import com.mindhub.HomeBanking.models.Client;

import java.util.List;

public interface ClientService  {
    List<ClientDTO> findAll();
    Client findById(Long id);
    ClientDTO getClientDTO(Long id);
    void save(Client client);
    Client findByEmail(String email);
}
