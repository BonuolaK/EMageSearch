package com.myproject.v1.dao;

import com.myproject.v1.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client,Integer> {

    Optional<Client> findFirstByDomainURLOrName(String domainURL, String name);

    Optional<Client> findFirstByClientId(UUID clientUUID);
}
