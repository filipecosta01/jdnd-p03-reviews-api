package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.entity.CommentDocument;
import com.udacity.course3.reviews.entity.ReviewDocument;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class ReviewDocumentRepositoryTest {

    final String collectionName = "reviewDocument";

    @AfterEach
    void afterEachTest(@Autowired MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection(ReviewDocument.class);
    }

    @Test
    void testCreateReview(@Autowired MongoTemplate mongoTemplate) {
        // given
        final ReviewDocument reviewDocument = createReviewDocument(1, "Test Title", "Test Message", 9);

        // when
        final ReviewDocument savedReview = mongoTemplate.save(reviewDocument, collectionName);

        // then
        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getTitle()).isEqualTo(reviewDocument.getTitle());
        assertThat(savedReview.getMessage()).isEqualTo(reviewDocument.getMessage());
        assertThat(savedReview.getRating()).isEqualTo(reviewDocument.getRating());
        assertThat(savedReview.getComments()).isNotEmpty();
        assertThat(savedReview.getComments()).hasSize(2);
    }

    @Test
    void testFindReviewById(@Autowired MongoTemplate mongoTemplate) {
        // given
        final ReviewDocument reviewDocument = createReviewDocument(2, "Test Title 2", "Test Message 2", 5);
        final ReviewDocument savedReview = mongoTemplate.save(reviewDocument, collectionName);

        // when
        final ReviewDocument reviewFromFind = mongoTemplate.findById(savedReview.getId(), ReviewDocument.class);

        // then
        assertThat(reviewFromFind).isNotNull();
        assertThat(reviewFromFind.getTitle()).isEqualTo(reviewDocument.getTitle());
        assertThat(reviewFromFind.getMessage()).isEqualTo(reviewDocument.getMessage());
        assertThat(reviewFromFind.getRating()).isEqualTo(reviewDocument.getRating());
        assertThat(savedReview.getComments()).isNotEmpty();
        assertThat(savedReview.getComments()).hasSize(2);
    }

    @Test
    void testFindAllReviews(@Autowired MongoTemplate mongoTemplate) {
        // given
        final ReviewDocument reviewDocument = createReviewDocument(3, "Test Title 3", "Test Message 3", 3);
        final ReviewDocument reviewDocument2 = createReviewDocument(4, "Test Title 4", "Test Message 4", 3);
        final ReviewDocument savedReview = mongoTemplate.save(reviewDocument, collectionName);
        final ReviewDocument savedReview2 = mongoTemplate.save(reviewDocument2, collectionName);
        final List<ReviewDocument> reviewDocuments = Arrays.asList(savedReview, savedReview2);

        // when
        final List<ReviewDocument> reviewsFromFind = mongoTemplate.findAll(ReviewDocument.class);

        // then
        assertThat(reviewsFromFind).isNotNull();
        assertThat(reviewsFromFind).hasSize(2);

        reviewsFromFind.forEach(fromFindDocument -> {
            final ReviewDocument auxReviewDocument = reviewDocuments.stream()
                                                                    .filter(document -> document.getId().equals(fromFindDocument.getId()))
                                                                    .findFirst()
                                                                    .get();
            assertThat(fromFindDocument.getId()).isEqualTo(auxReviewDocument.getId());
            assertThat(fromFindDocument.getTitle()).isEqualTo(auxReviewDocument.getTitle());
            assertThat(fromFindDocument.getMessage()).isEqualTo(auxReviewDocument.getMessage());
            assertThat(fromFindDocument.getRating()).isEqualTo(auxReviewDocument.getRating());
            assertThat(fromFindDocument.getComments()).isNotEmpty();
            assertThat(fromFindDocument.getComments()).hasSize(2);
        });
    }

    ReviewDocument createReviewDocument(Integer id, String title, String message, Integer rating) {
        final ReviewDocument reviewDocument = new ReviewDocument();
        reviewDocument.setId(id);
        reviewDocument.setRating(rating);
        reviewDocument.setTitle(title);
        reviewDocument.setMessage(message);

        final CommentDocument commentDocument = new CommentDocument();
        commentDocument.setTitle(title + " Comment 1");
        commentDocument.setMessage(title + " Message 1");
        commentDocument.setId(id + 1);

        final CommentDocument commentDocument2 = new CommentDocument();
        commentDocument2.setTitle(title + " Comment 2");
        commentDocument2.setMessage(title + " Message 2");
        commentDocument2.setId(id + 2);

        reviewDocument.setComments(Arrays.asList(commentDocument, commentDocument2));

        return reviewDocument;
    }
}
