CREATE TABLE flag_audit (
    id BIGSERIAL PRIMARY KEY,
    flag_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    changed_by VARCHAR(100) NOT NULL,
    changed_at TIMESTAMP NOT NULL,
    FOREIGN KEY (flag_id) REFERENCES flags(id) ON DELETE CASCADE
);
