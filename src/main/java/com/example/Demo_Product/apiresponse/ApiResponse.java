package com.example.Demo_Product.apiresponse;

import com.example.Demo_Product.model.CardDTO;

import java.util.List;

public class ApiResponse {

    private List<CardDTO> cardsList;

    public ApiResponse(List<CardDTO> cardsList) {
        this.cardsList = cardsList;
    }

    public List<CardDTO> getCardsList() {
        return cardsList; // Getter for cardsList
    }

    public void setCardsList(List<CardDTO> cardsList) {
        this.cardsList = cardsList; // Setter for cardsList
    }
}
