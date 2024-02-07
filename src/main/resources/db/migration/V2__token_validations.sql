CREATE TABLE token_validations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fk_user_id BIGINT,
    token VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    token_expires TIMESTAMP NOT NULL,
    created_by BIGINT,
    last_modified_by BIGINT,
    last_modified_at TIMESTAMP,
    active BIT DEFAULT 0,
    CONSTRAINT token_validations_fk_user_id FOREIGN KEY (fk_user_id)
        REFERENCES user_info (user_id),
    CONSTRAINT token_validations_created_by FOREIGN KEY (created_by)
        REFERENCES user_info (user_id),
    CONSTRAINT token_validations_last_modified_by FOREIGN KEY (last_modified_by)
        REFERENCES user_info (user_id)
);