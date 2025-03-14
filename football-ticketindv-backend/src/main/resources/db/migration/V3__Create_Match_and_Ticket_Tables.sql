CREATE TABLE `match` (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         home_club_id BIGINT NOT NULL,
                         away_club_id BIGINT NOT NULL,
                         match_date_time DATETIME NOT NULL,
                         competition_id BIGINT NOT NULL,
                         PRIMARY KEY (id),
                         FOREIGN KEY (home_club_id) REFERENCES club(id),
                         FOREIGN KEY (away_club_id) REFERENCES club(id),
                         FOREIGN KEY (competition_id) REFERENCES competition(id)
);

CREATE TABLE ticket (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        match_id BIGINT NOT NULL,
                        purchase_date_time DATETIME NOT NULL,
                        total_price DOUBLE NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (match_id) REFERENCES `match`(id)
);

CREATE TABLE ticket_seat (
                             ticket_id BIGINT NOT NULL,
                             seat_id BIGINT NOT NULL,
                             PRIMARY KEY (ticket_id, seat_id),
                             FOREIGN KEY (ticket_id) REFERENCES ticket(id),
                             FOREIGN KEY (seat_id) REFERENCES seat(id)
);