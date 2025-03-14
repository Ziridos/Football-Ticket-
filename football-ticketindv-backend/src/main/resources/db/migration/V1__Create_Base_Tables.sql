CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(20) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       address VARCHAR(255) NOT NULL,
                       phone VARCHAR(15) NOT NULL,
                       country VARCHAR(255) NOT NULL,
                       city VARCHAR(255) NOT NULL,
                       postal_code VARCHAR(255) NOT NULL,
                       _is_admin BOOLEAN NOT NULL,
                       PRIMARY KEY (id),
                       UNIQUE (email)
);

CREATE TABLE stadium (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         stadium_name VARCHAR(255) NOT NULL,
                         stadium_address VARCHAR(255) NOT NULL,
                         stadium_postal_code VARCHAR(255) NOT NULL,
                         stadium_city VARCHAR(255) NOT NULL,
                         stadium_country VARCHAR(255) NOT NULL,
                         PRIMARY KEY (id),
                         UNIQUE (stadium_name)
);

CREATE TABLE club (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      club_name VARCHAR(255) NOT NULL,
                      stadium_id BIGINT,
                      PRIMARY KEY (id),
                      UNIQUE (club_name),
                      FOREIGN KEY (stadium_id) REFERENCES stadium(id)
);

CREATE TABLE competition (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             competition_name VARCHAR(255) NOT NULL,
                             PRIMARY KEY (id),
                             UNIQUE (competition_name)
);