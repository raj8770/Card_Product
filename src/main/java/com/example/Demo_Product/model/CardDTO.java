package com.example.Demo_Product.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({ "id", "cardDetails" })  // Pehle ID aayega, phir details
public class CardDTO {
    private Long id;
    private CardDetails cardDetails;

    public CardDTO(Long id, String title, String description, String imageUrl, Long price) {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardDetails getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(CardDetails cardDetails) {
        this.cardDetails = cardDetails;
    }


    public CardDTO(Long id, CardDetails cardDetails) {
        this.id = id;
        this.cardDetails = cardDetails;
    }

    public CardDTO() {
    }
}
