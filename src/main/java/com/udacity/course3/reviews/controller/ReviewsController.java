package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.entity.ReviewDocument;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewDocumentRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
public class ReviewsController {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ReviewDocumentRepository reviewDocumentRepository;

    /**
     * Creates a review for a product.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.POST)
    public ResponseEntity<?> createReviewForProduct(@PathVariable("productId") Integer productId, @RequestBody Review review) {
        final Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        review.setProduct(optionalProduct.get());

        final Review savedReview = reviewRepository.save(review);
        final ReviewDocument reviewDocument = new ReviewDocument();

        reviewDocument.setId(savedReview.getId());
        reviewDocument.setTitle(savedReview.getTitle());
        reviewDocument.setRating(savedReview.getRating());
        reviewDocument.setMessage(savedReview.getMessage());
        reviewDocument.setCreatedAt(savedReview.getCreatedAt() != null ? Date.from(savedReview.getCreatedAt().toInstant()) : Date.from(Instant.now()));
        reviewDocument.setComments(new ArrayList<>());
        reviewDocumentRepository.save(reviewDocument);

        return ResponseEntity.ok(savedReview);
    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<List<?>> listReviewsForProduct(@PathVariable("productId") Integer productId) {
        final Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        final Optional<List<Review>> optionalReviews = reviewRepository.findAllByProduct(optionalProduct.get());
        if (!optionalReviews.isPresent()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        final List<Integer> reviewIds = optionalReviews.get().stream().map(Review::getId).collect(Collectors.toList());

        final Iterable<ReviewDocument> iterableDocuments = reviewDocumentRepository.findAllById(reviewIds);

        final List<ReviewDocument> reviewDocuments = new ArrayList<>();
        iterableDocuments.forEach(reviewDocuments::add);

        return ResponseEntity.ok(reviewDocuments);
    }
}