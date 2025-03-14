ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';

UPDATE users SET role = CASE
                            WHEN _is_admin = true THEN 'ADMIN'
                            ELSE 'USER'
    END;

ALTER TABLE users DROP COLUMN _is_admin;