package com.myproject.v1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myproject.v1.model.enums.ProductType;

import javax.persistence.*;

@Entity
public class ClientProduct {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="client_id", nullable = true)
    private Client client;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    public ClientProduct() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
