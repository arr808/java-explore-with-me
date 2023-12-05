CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  name VARCHAR(250) NOT NULL,
  email VARCHAR(254) NOT NULL,
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  lat REAL NOT NULL,
  lon REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  initiator_id BIGINT NOT NULL,
  title VARCHAR(120) NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  description VARCHAR(7000) NOT NULL,
  category_id BIGINT NOT NULL,
  event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  location_id BIGINT NOT NULL,
  paid BOOLEAN NOT NULL,
  participant_limit INT NOT NULL,
  confirmed_requests INT NOT NULL,
  request_moderation BOOLEAN NOT NULL,
  created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  published_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  state VARCHAR(15) NOT NULL,
  views BIGINT NOT NULL,
  CONSTRAINT FK_INITIATOR_ID_users FOREIGN KEY (initiator_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT FK_CATEGORY_ID_CATEGORIES FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
  CONSTRAINT FK_LOCATION_ID_LOCATIONS FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests(
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  event_id BIGINT NOT NULL,
  requester_id BIGINT NOT NULL REFERENCES users (id),
  status VARCHAR(40) NOT NULL,
  CONSTRAINT FK_EVENT_ID_EVENTS FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
  CONSTRAINT FK_REQUESTER_ID_USERS FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations(
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  pinned BOOLEAN NOT NULL,
  title VARCHAR(50) NOT NULL,
  CONSTRAINT UQ_COMPILATION_TITLE UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS event_compilations(
  event_id BIGINT NOT NULL,
  compilation_id BIGINT NOT NULL,
  PRIMARY KEY (event_id, compilation_id),
  CONSTRAINT FKpk_EVENT_ID_EVENTS FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
  CONSTRAINT FKpk_COMPILATION_ID_COMPILATIONS FOREIGN KEY (compilation_id) REFERENCES compilations(id) ON DELETE CASCADE
);