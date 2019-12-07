package com.udacity.course3.reviews.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
public class ReviewRepositoryTest {

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

    @Test
    public void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(testEntityManager).isNotNull();
        assertThat(productRepository).isNotNull();
        assertThat(reviewRepository).isNotNull();
    }

    @Test
    public void testCreateReview() {
        // given
        final Product savedProduct = productRepository.save(createProduct("Test Name", "Test description", new Double("99.4")));
        final Review review = createReview("Test Review", "", 10, savedProduct);

        // when
        final Review savedReview = reviewRepository.save(review);

        // then
        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getTitle()).isEqualTo(review.getTitle());
        assertThat(savedReview.getMessage()).isEqualTo(review.getMessage());
        assertThat(savedReview.getRating()).isEqualTo(review.getRating());
        assertThat(savedReview.getProduct()).isEqualTo(savedProduct);

    }

    @Test
    public void testFindReviewById() {
        // given
        final Product savedProduct = productRepository.save(createProduct("Test Name", "Test description", new Double("99.4")));
        final Review review = createReview("Test Review", "", 10, savedProduct);
        final Review savedReview = reviewRepository.save(review);

        // when
        final Optional<Review> reviewFromFind = reviewRepository.findById(savedReview.getId());

        // then
        assertThat(reviewFromFind).isNotNull();
        assertThat(reviewFromFind.isPresent()).isEqualTo(true);
        assertThat(reviewFromFind.get().getTitle()).isEqualTo(review.getTitle());
        assertThat(reviewFromFind.get().getMessage()).isEqualTo(review.getMessage());
        assertThat(reviewFromFind.get().getRating()).isEqualTo(review.getRating());
        assertThat(reviewFromFind.get().getProduct()).isEqualTo(savedProduct);

    }

    @Test
    public void testFindAllReviewsByProductId() {
        // given
        final Product savedProduct = productRepository.save(createProduct("Test Name", "Test description", new Double("99.4")));
        final Review review = createReview("Test Review", "", 10, savedProduct);
        final Review review2 = createReview("Test Review 2", "", 9, savedProduct);
        entityManager.persist(review);
        entityManager.persist(review2);

        // when
        final Optional<List<Review>> reviewsFromFind = reviewRepository.findAllByProduct(savedProduct);

        // then
        assertThat(reviewsFromFind).isNotNull();
        assertThat(reviewsFromFind.isPresent()).isEqualTo(true);
        assertThat(reviewsFromFind.get()).hasSize(2);
        assertThat(reviewsFromFind.get()).contains(review);
        assertThat(reviewsFromFind.get()).contains(review2);
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
}
