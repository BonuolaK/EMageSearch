package com.myproject.v1.viewmodel;

import com.myproject.v1.model.Client;

public class ClientDetailsVM {
    private Client client;
    private String code;

    public ClientDetailsVM() {
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
