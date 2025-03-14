CREATE TABLE box_pricing_rule (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  stadium_id BIGINT NOT NULL,
                                  occupancy_threshold DOUBLE NOT NULL,
                                  price_increase_percentage DOUBLE NOT NULL,
                                  PRIMARY KEY (id),
                                  FOREIGN KEY (stadium_id) REFERENCES stadium(id)
);

CREATE TABLE match_specific_box_price (
                                          match_id BIGINT NOT NULL,
                                          box_id BIGINT NOT NULL,
                                          price DOUBLE NOT NULL,
                                          PRIMARY KEY (match_id, box_id),
                                          FOREIGN KEY (match_id) REFERENCES `match`(id),
                                          FOREIGN KEY (box_id) REFERENCES box(id)
);

CREATE TABLE match_specific_seat_price (
                                           match_id BIGINT NOT NULL,
                                           seat_id BIGINT NOT NULL,
                                           price DOUBLE NOT NULL,
                                           PRIMARY KEY (match_id, seat_id),
                                           FOREIGN KEY (match_id) REFERENCES `match`(id),
                                           FOREIGN KEY (seat_id) REFERENCES seat(id)
);

CREATE TABLE match_available_seat (
                                      match_id BIGINT NOT NULL,
                                      seat_id BIGINT NOT NULL,
                                      is_available BOOLEAN NOT NULL,
                                      PRIMARY KEY (match_id, seat_id),
                                      FOREIGN KEY (match_id) REFERENCES `match`(id),
                                      FOREIGN KEY (seat_id) REFERENCES seat(id)
);