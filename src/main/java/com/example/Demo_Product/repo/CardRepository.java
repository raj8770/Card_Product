package com.example.Demo_Product.repo;


import com.example.Demo_Product.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    // Find cards by title (case-insensitive search)
    List<Card> findByTitleContainingIgnoreCase(String title);


    // Custom query to filter by title OR description AND price range
    @Query("SELECT c FROM Card c WHERE " +
            "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) AND " +
            "c.price BETWEEN :minPrice AND :maxPrice")
    List<Card> searchCards(@Param("keyword") String keyword,
                           @Param("minPrice") Long minPrice,
                           @Param("maxPrice") Long maxPrice);
}




