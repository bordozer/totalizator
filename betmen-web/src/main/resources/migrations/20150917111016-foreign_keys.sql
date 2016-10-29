  ALTER TABLE categories ADD CONSTRAINT fk_sportKind_id FOREIGN KEY (sportKindId) REFERENCES sportKinds(id);
