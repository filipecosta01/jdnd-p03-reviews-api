package com.udacity.course3.reviews.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.udacity.course3.reviews.entity.Product;
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
public class ProductRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(testEntityManager).isNotNull();
        assertThat(productRepository).isNotNull();
    }

    @Test
    public void testCreateProduct() {
        // given
        final Product product = createProduct("Test Name", "Test description", new Double("99.4"));

        // when
        final Product savedProduct = productRepository.save(product);

        // then
        assertThat(savedProduct).isNotNull();
        assertThat(product.getName()).isEqualTo(savedProduct.getName());
        assertThat(product.getValue()).isEqualTo(savedProduct.getValue());
        assertThat(product.getDescription()).isEqualTo(savedProduct.getDescription());
    }

    @Test
    public void testFindProductById() {
        // given
        final Product product = createProduct("Test Name", "Test description", new Double("99.4"));
        final Product savedProduct = productRepository.save(product);

        // when
        final Optional<Product> productFromFind = productRepository.findById(savedProduct.getId());

        // then
        assertThat(productFromFind.isPresent()).isEqualTo(true);

        assertThat(productFromFind.get().getName()).isEqualTo(product.getName());
        assertThat(productFromFind.get().getDescription()).isEqualTo(product.getDescription());
        assertThat(productFromFind.get().getValue()).isEqualTo(product.getValue());
    }

    @Test
    public void testFindAllProducts() {
        // given
        final Product product = createProduct("Test Name", "Test description", new Double("99.4"));
        final Product product2 = createProduct("Test Name 2", "Test Description 2", new Double("98.4"));

        entityManager.persist(product);
        entityManager.persist(product2);

        // when
        final List<Product> products = productRepository.findAll();

        // then
        assertThat(products).isNotEmpty();
        assertThat(products).hasSize(2);
        assertThat(products).contains(product);
        assertThat(products).contains(product2);
    }

    Product createProduct(String name, String description, Double value) {
        final Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setValue(value);
        return product;
    }
}
