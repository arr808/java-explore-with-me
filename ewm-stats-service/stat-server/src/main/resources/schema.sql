DROP TABLE IF EXISTS hit CASCADE;

CREATE TABLE IF NOT EXISTS hit (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  app VARCHAR NOT NULL,
  uri VARCHAR NOT NULL,
  ip VARCHAR NOT NULL,
  timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);