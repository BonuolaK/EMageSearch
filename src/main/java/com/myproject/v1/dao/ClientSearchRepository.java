package com.myproject.v1.dao;

import com.myproject.v1.model.ClientSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientSearchRepository extends JpaRepository<ClientSearch, Integer> {
}
