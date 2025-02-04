package com.example.Demo_Product.service;

import com.example.Demo_Product.model.Card;
import com.example.Demo_Product.model.CardDTO;
import com.example.Demo_Product.model.CardDetails;
import com.example.Demo_Product.repo.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;


    public List<Card> getAlllCards() {
        return cardRepository.findAll();
    }

    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }

    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    public Card updateCard(Long id, Card updatedCard) {
        return cardRepository.findById(id)
                .map(card -> {
                    card.setTitle(updatedCard.getTitle());
                    card.setDescription(updatedCard.getDescription());
                    card.setImageUrl(updatedCard.getImageUrl());
                    card.setPrice(updatedCard.getPrice());
                    return cardRepository.save(card);
                })
                .orElseThrow(() -> new RuntimeException("Card not found with id " + id));
    }

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }


    //formal
    public List<CardDTO> getAllCards() {
        List<Card> cards = cardRepository.findAll();
        return cards.stream().map(card -> new CardDTO(
                card.getId(),
                new CardDetails(
                        card.getTitle(),
                        card.getDescription(),
                        card.getImageUrl(),
                        card.getPrice()
                )
        )).collect(Collectors.toList());
    }




}
