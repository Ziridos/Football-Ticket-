CREATE TABLE box (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     box_name VARCHAR(255) NOT NULL,
                     x_position INT NOT NULL,
                     y_position INT NOT NULL,
                     width INT NOT NULL,
                     height INT NOT NULL,
                     stadium_id BIGINT NOT NULL,
                     price DOUBLE DEFAULT 0.0,
                     PRIMARY KEY (id),
                     UNIQUE (box_name, stadium_id),
                     FOREIGN KEY (stadium_id) REFERENCES stadium(id)
);

CREATE TABLE block (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       block_name VARCHAR(255) NOT NULL,
                       x_position INT NOT NULL,
                       y_position INT NOT NULL,
                       width INT NOT NULL,
                       height INT NOT NULL,
                       box_id BIGINT NOT NULL,
                       PRIMARY KEY (id),
                       UNIQUE (block_name, box_id),
                       FOREIGN KEY (box_id) REFERENCES box(id)
);

CREATE TABLE seat (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      seat_number VARCHAR(255) NOT NULL,
                      x_position INT NOT NULL,
                      y_position INT NOT NULL,
                      block_id BIGINT NOT NULL,
                      price DOUBLE DEFAULT 0.0,
                      PRIMARY KEY (id),
                      UNIQUE (seat_number, block_id),
                      FOREIGN KEY (block_id) REFERENCES block(id)
);