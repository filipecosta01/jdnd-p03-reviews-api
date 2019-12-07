package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReviewRepository reviewRepository;

    /**
     * Creates a comment for a review.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.POST)
    public ResponseEntity<?> createCommentForReview(@PathVariable("reviewId") Integer reviewId, @RequestBody Comment comment) {
        final Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (!optionalReview.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        comment.setReview(optionalReview.get());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    /**
     * List comments for a review.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.GET)
    public ResponseEntity<List<?>> listCommentsForReview(@PathVariable("reviewId") Integer reviewId) {
        final Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (!optionalReview.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        final Optional<List<Comment>> optionalComments = commentRepository.findAllByReview(optionalReview.get());
        if (!optionalComments.isPresent()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(optionalComments.get());
    }
}