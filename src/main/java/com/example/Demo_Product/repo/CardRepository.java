package com.example.Demo_Product.repo;


import com.example.Demo_Product.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    // Find cards by title (case-insensitive search)
    List<Card> findByTitleContainingIgnoreCase(String title);
}

