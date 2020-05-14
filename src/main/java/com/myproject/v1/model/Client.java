package com.myproject.v1.model;

import com.myproject.v1.model.enums.*;
import com.sun.jna.platform.win32.Guid;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
    public class Client {
        public Client(){}

        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        private int id;
        @Column(columnDefinition="VARCHAR(36)")
        private UUID clientId;
        private String name;
        @Enumerated(EnumType.STRING)
        private DomainType domainType;
        private String domainURL;

        @EqualsAndHashCode.Exclude
        @OneToMany(targetEntity = ClientProduct.class,mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
        private List<ClientProduct> productTypes = new ArrayList<>();

        @Enumerated(EnumType.STRING)
        private ResponseType responseType;

        private String responseUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DomainType getDomainType() {
            return domainType;
        }

        public void setDomainType(DomainType domainType) {
            this.domainType = domainType;
        }

        public String getDomainURL() {
            return domainURL;
        }

        public void setDomainURL(String domainURL) {
            this.domainURL = domainURL;
        }

        public List<ClientProduct> getProductTypes() {
            return productTypes;
        }

        public void setProductTypes(List<ClientProduct> productTypes) {
            this.productTypes = productTypes;
        }

        public ResponseType getResponseType() {
            return responseType;
        }

        public void setResponseType(ResponseType responseType) {
            this.responseType = responseType;
        }

        public String getResponseUrl() {
            return responseUrl;
        }

        public void setResponseUrl(String responseUrl) {
            this.responseUrl = responseUrl;
        }

        public String getShopUrl(){
            String url = "";
            url =  this.getDomainType().equals(DomainType.HTTP) ? "http://": "https://";
            url = url + this.getDomainURL();
            return url;
        }

    public String getRedirectUrl(ProductType type, String color, String tag){
        if(this.getResponseType() == ResponseType.CALL_BACK)
            return null;
        else{
            String url = this.getResponseUrl();
            url = url.replace("{{PRODUCT_TYPE}}",type.toString());
            url = url.replace("{{PRODUCT_COLOR}}",color);
            url = url.replace("{{PRODUCT_TAG}}",tag);

        return url;
        }

    }
}

