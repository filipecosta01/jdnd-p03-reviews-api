package com.udacity.course3.reviews.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(testEntityManager).isNotNull();
        assertThat(productRepository).isNotNull();
        assertThat(reviewRepository).isNotNull();
        assertThat(commentRepository).isNotNull();
    }

    @Test
    public void testCreateComment() {
        // given
        final Product savedProduct = productRepository.save(createProduct("Test Name", "Test description", new Double("99.4")));
        final Review savedReview = reviewRepository.save(createReview("Test Review", "", 10, savedProduct));
        final Comment comment = createComment("Test Comment", "", savedReview);

        // when
        final Comment savedComment = commentRepository.save(comment);

        // then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getTitle()).isEqualTo(comment.getTitle());
        assertThat(savedComment.getMessage()).isEqualTo(comment.getMessage());
        assertThat(savedComment.getReview()).isEqualTo(savedReview);
    }

    @Test
    public void testFindCommentById() {
        // given
        final Product savedProduct = productRepository.save(createProduct("Test Name", "Test description", new Double("99.4")));
        final Review savedReview = reviewRepository.save(createReview("Test Review", "", 10, savedProduct));
        final Comment comment = createComment("Test Comment", "", savedReview);
        final Comment savedComment = commentRepository.save(comment);

        // when
        final Optional<Comment> commentFromFind = commentRepository.findById(savedComment.getId());

        // then
        assertThat(commentFromFind).isNotNull();
        assertThat(commentFromFind.isPresent()).isEqualTo(true);
        assertThat(commentFromFind.get().getTitle()).isEqualTo(comment.getTitle());
        assertThat(commentFromFind.get().getMessage()).isEqualTo(comment.getMessage());
        assertThat(commentFromFind.get().getReview()).isEqualTo(comment.getReview());
    }

    @Test
    public void testFindAllCommentsByReviewId() {
        // given
        final Product savedProduct = productRepository.save(createProduct("Test Name", "Test description", new Double("99.4")));
        final Review savedReview = reviewRepository.save(createReview("Test Review", "", 10, savedProduct));
        final Comment comment = createComment("Test Comment", "", savedReview);
        final Comment comment2 = createComment("Test Comment 2", "", savedReview);
        entityManager.persist(comment);
        entityManager.persist(comment2);

        // when
        final Optional<List<Comment>> commentsFromFind = commentRepository.findAllByReview(savedReview);

        // then
        assertThat(commentsFromFind).isNotNull();
        assertThat(commentsFromFind.isPresent()).isEqualTo(true);
        assertThat(commentsFromFind.get()).hasSize(2);
        assertThat(commentsFromFind.get()).contains(comment);
        assertThat(commentsFromFind.get()).contains(comment2);
    }

    Product createProduct(String name, String description, Double value) {
        final Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setValue(value);

        return product;
    }

    Review createReview(String title, String message, Integer rating, Product product) {
        final Review review = new Review();
        review.setTitle(title);
        review.setMessage(message);
        review.setRating(rating);
        review.setProduct(product);

        return review;
    }

    Comment createComment(String title, String message, Review review) {
        final Comment comment = new Comment();
        comment.setTitle(title);
        comment.setMessage(message);
        comment.setReview(review);

        return comment;
    }
}
