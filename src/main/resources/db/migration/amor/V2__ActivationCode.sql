CREATE SEQUENCE seq_activation_code
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


CREATE TABLE activation_code (
  id           BIGINT DEFAULT nextval('seq_activation_code' :: REGCLASS) NOT NULL,
  version      BIGINT                                            NOT NULL,
  date_created TIMESTAMP WITHOUT TIME ZONE                       NOT NULL,
  last_updated TIMESTAMP WITHOUT TIME ZONE                       NOT NULL,
  user_id      BIGINT,
  expire_date  TIMESTAMP WITHOUT TIME ZONE                       NOT NULL,
  code         CHARACTER VARYING(255)
);

ALTER TABLE ONLY activation_code
  ADD CONSTRAINT activation_code_pkey PRIMARY KEY (id);

ALTER TABLE ONLY activation_code
  ADD CONSTRAINT seq_activation_code_user_fkey FOREIGN KEY (user_id) REFERENCES app_user (id);

SELECT pg_catalog.setval('seq_activation_code', 1, FALSE);