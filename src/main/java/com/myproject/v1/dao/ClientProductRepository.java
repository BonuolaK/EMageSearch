package com.myproject.v1.dao;

import com.myproject.v1.model.ClientProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientProductRepository extends JpaRepository<ClientProduct, Integer> {
}
