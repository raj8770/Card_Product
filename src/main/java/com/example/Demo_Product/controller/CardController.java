package com.example.Demo_Product.controller;


import com.example.Demo_Product.apiresponse.ApiResponse;
import com.example.Demo_Product.model.Card;
import com.example.Demo_Product.model.CardDTO;
import com.example.Demo_Product.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;



    @GetMapping
    public List<Card> getAlllCards() {
        return cardService.getAlllCards();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        Optional<Card> card = cardService.getCardById(id);
        return card.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public Card createCard(@RequestBody Card card) {
        return cardService.createCard(card);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody Card updatedCard) {
        return ResponseEntity.ok(cardService.updateCard(id, updatedCard));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/formal")
    public ApiResponse getAllCards() {
        List<CardDTO> cardDTOs = cardService.getAllCards();
        return new ApiResponse(cardDTOs); // Wraps the cards inside the "cards" object
    }


}
