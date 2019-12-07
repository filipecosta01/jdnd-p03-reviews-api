package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    /**
     * Find reviews by product
     * @param product valid and existent product
     * @return list of reviews as per product in parameter if exists in database
     */
    Optional<List<Review>> findAllByProduct(Product product);
}
