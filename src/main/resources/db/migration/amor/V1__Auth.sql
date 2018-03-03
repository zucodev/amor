CREATE SEQUENCE seq_user
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


CREATE TABLE app_user (
  id               BIGINT DEFAULT nextval('seq_user' :: REGCLASS) NOT NULL,
  version          BIGINT                                         NOT NULL,
  date_created     TIMESTAMP WITHOUT TIME ZONE                    NOT NULL,
  last_updated     TIMESTAMP WITHOUT TIME ZONE                    NOT NULL,
  email            CHARACTER VARYING(255)                         NOT NULL,
  password         CHARACTER VARYING(255)                         NOT NULL,
  first_name       CHARACTER VARYING(255),
  last_name        CHARACTER VARYING(255),
  enabled          BOOLEAN                                        NOT NULL,
  activated        BOOLEAN                                        NOT NULL,
  password_expired BOOLEAN                                        NOT NULL,
  account_expired  BOOLEAN                                        NOT NULL,
  account_locked   BOOLEAN                                        NOT NULL
);

ALTER TABLE ONLY app_user
  ADD CONSTRAINT user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY app_user
  ADD CONSTRAINT uk_user_email UNIQUE (email);

CREATE SEQUENCE seq_role
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


CREATE TABLE role (
  id        BIGINT DEFAULT nextval('seq_role' :: REGCLASS) NOT NULL,
  version   BIGINT                                         NOT NULL,
  authority CHARACTER VARYING(255)                         NOT NULL,
  type      CHARACTER VARYING(255)
);


ALTER TABLE ONLY role
  ADD CONSTRAINT role_pkey PRIMARY KEY (id);


ALTER TABLE ONLY role
  ADD CONSTRAINT uk_role_authority UNIQUE (authority);

CREATE TABLE user_role (
  role_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL
);


ALTER TABLE ONLY user_role
  ADD CONSTRAINT user_role_pkey PRIMARY KEY (role_id, user_id);

ALTER TABLE ONLY user_role
  ADD CONSTRAINT user_role_user_fkey FOREIGN KEY (user_id) REFERENCES app_user (id);

ALTER TABLE ONLY user_role
  ADD CONSTRAINT user_role_role_fkey FOREIGN KEY (role_id) REFERENCES role (id);


SELECT pg_catalog.setval('seq_user', 1, FALSE);
SELECT pg_catalog.setval('seq_role', 1, FALSE);