package com.myproject.v1.service;

import com.myproject.v1.dao.ClientProductRepository;
import com.myproject.v1.dao.ClientRepository;
import com.myproject.v1.dao.ClientSearchRepository;
import com.myproject.v1.dao.UserRepository;
import com.myproject.v1.model.Client;
import com.myproject.v1.model.ClientProduct;
import com.myproject.v1.model.ClientSearch;
import com.myproject.v1.model.User;
import com.myproject.v1.model.enums.ProductType;
import com.myproject.v1.viewmodel.RegistrationModel;
import com.myproject.v1.viewmodel.ResultModel;
import com.myproject.v1.viewmodel.SearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientSearchRepository clientSearchRepository;

    @Autowired
    private ClientProductRepository clientProdRepository;



    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public ResultModel<Client> CreateClient(RegistrationModel model){

        ResultModel<Client> result = new ResultModel<>();
        Optional<Client> client = clientRepository.findFirstByDomainURLOrName(model.getDomainURL(),model.getName());
        Optional<User> user = userRepository.findFirstByEmail(model.getEmail());


        if(client.isPresent()){
            result.setHasError(true);
            Client clientEntity = client.get();
            if(model.getName() == clientEntity.getName())
                result.setMessage("Name already exists");
            else
                result.setMessage("Domain url already exists");
            return result;
        }

        if(user.isPresent()){
            result.setHasError(true);
            result.setMessage("Email already exists");
            return result;
        }

        Client newClient = new Client();
        newClient.setClientId(UUID.randomUUID());
        newClient.setDomainType(model.getDomainType());
        newClient.setDomainURL(model.getDomainURL());
        newClient.setName(model.getName());
        newClient.setResponseType(model.getResponseType());
        newClient.setResponseUrl(model.getResponseUrl());
        newClient = clientRepository.save(newClient);

        for (ProductType type:model.getProductTypes()) {
            ClientProduct product = new ClientProduct();
            product.setClient(newClient);
            product.setProductType(type);
            clientProdRepository.save(product);
        }

        // add user
        User newUser = new User();
        newUser.setAdmin(false);
        newUser.setClient(newClient);
        newUser.setEmail(model.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));
        userRepository.save(newUser);

        result.setData(newClient);

        return result;
    }


    @Transactional
    public void AddSearch(SearchModel model, Optional<Client> client, String fileName){

        ResultModel<ClientSearch> result = new ResultModel<>();

        ClientSearch clientSearch = new ClientSearch();

        if(client.isPresent())
            clientSearch.setClient(client.get());

        clientSearch.setColor(model.getColor());
        clientSearch.setProductType(model.getProductType());
        clientSearch.setTag(model.getTag());
        clientSearch.setFileName(fileName);

        clientSearchRepository.save(clientSearch);

    }


    public Optional<Client> getClientByClientId(String clientId){
        UUID clientUUID = UUID.fromString(clientId);
        return clientRepository.findFirstByClientId(clientUUID);
    }
}
