package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Find comments by review
     * @param review valid and existent review
     * @return list of comments as per review in parameter if exists in database
     */
    Optional<List<Comment>> findAllByReview(Review review);
}
