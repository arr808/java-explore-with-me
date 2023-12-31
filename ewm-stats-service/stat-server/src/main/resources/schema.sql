DROP TABLE IF EXISTS stat CASCADE;

CREATE TABLE IF NOT EXISTS stat (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  app VARCHAR(50) NOT NULL,
  uri VARCHAR(50) NOT NULL,
  ip VARCHAR(16) NOT NULL,
  timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);