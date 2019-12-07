CREATE TABLE product (
    id INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    value DOUBLE NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE review (
    id INT AUTO_INCREMENT,
    product_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    rating INT NOT NULL,
    message VARCHAR(500) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    FOREIGN KEY(product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE TABLE comment (
    id INT AUTO_INCREMENT,
    review_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    message VARCHAR(500) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE
);


